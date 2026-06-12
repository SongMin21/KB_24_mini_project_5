// GitHub 푸시 -> Notion 작업로그 누적 스크립트
// GitHub Actions의 push 이벤트에서 실행됩니다.
//
// 필요한 환경변수 (워크플로에서 주입):
//   NOTION_TOKEN     : Notion 내부 통합(Integration) 토큰
//   TEAM_DB_ID       : '팀원' 데이터베이스 ID
//   WORKLOG_DB_ID    : '작업로그' 데이터베이스 ID
//   GH_ACTOR         : 푸시한 사람의 GitHub 아이디 (github.actor)
//   GITHUB_EVENT_PATH: 푸시 이벤트 JSON 경로 (Actions가 자동 제공)

const { Client } = require("@notionhq/client");
const fs = require("fs");

// ── Notion DB의 "속성 이름"과 정확히 일치시켜야 합니다 ──────────────
// (한글 그대로 만들었다면 아래 그대로 두면 됩니다)
const PROP = {
  teamName: "이름",            // 팀원 DB: 제목(title) 속성 (예: 강민주)
  teamGithub: "GitHub 사용자명", // 팀원 DB: 텍스트 속성 (예: minju-kang)
  logTitle: "제목",            // 작업로그 DB: 제목(title) 속성
  logDate: "날짜",             // 작업로그 DB: 날짜(date) 속성
  logMember: "팀원",           // 작업로그 DB: 관계형(relation) 속성 -> 팀원 DB
  logCount: "커밋 수",          // 작업로그 DB: 숫자(number) 속성
};

const notion = new Client({ auth: process.env.NOTION_TOKEN });
const TEAM_DB = process.env.TEAM_DB_ID;
const WORKLOG_DB = process.env.WORKLOG_DB_ID;

// 한국 시간(KST, UTC+9) 기준 YYYY-MM-DD
function kstDate() {
  const now = new Date(Date.now() + 9 * 60 * 60 * 1000);
  return now.toISOString().slice(0, 10);
}

// 팀원 DB에서 GitHub 아이디로 팀원 페이지 찾기
async function findTeamMember(login) {
  const res = await notion.databases.query({
    database_id: TEAM_DB,
    filter: { property: PROP.teamGithub, rich_text: { equals: login } },
  });
  return res.results[0] || null;
}

// 같은 날짜 + 같은 팀원의 작업로그 행 찾기
async function findLog(dateStr, memberId) {
  const res = await notion.databases.query({
    database_id: WORKLOG_DB,
    filter: {
      and: [
        { property: PROP.logDate, date: { equals: dateStr } },
        { property: PROP.logMember, relation: { contains: memberId } },
      ],
    },
  });
  return res.results[0] || null;
}

// 커밋 목록 -> Notion 글머리 기호 블록 (메시지 + 커밋 링크)
function commitBlocks(commits, branch) {
  return commits.map((c) => {
    const msg = c.message.split("\n")[0]; // 첫 줄만
    return {
      object: "block",
      type: "bulleted_list_item",
      bulleted_list_item: {
        rich_text: [
          { type: "text", text: { content: `[${branch}] ${msg} ` } },
          { type: "text", text: { content: "(커밋 보기)", link: { url: c.url } } },
        ],
      },
    };
  });
}

async function main() {
  const event = JSON.parse(fs.readFileSync(process.env.GITHUB_EVENT_PATH, "utf8"));
  const commits = event.commits || [];

  if (commits.length === 0) {
    console.log("커밋이 없는 푸시(태그/브랜치 삭제 등) — 건너뜀");
    return;
  }

  const branch = (event.ref || "").replace("refs/heads/", "");
  const login = process.env.GH_ACTOR;
  const dateStr = kstDate();

  const member = await findTeamMember(login);
  if (!member) {
    console.log(`⚠️  팀원 DB에 GitHub 아이디 '${login}'가 없습니다 — 건너뜀`);
    return; // 등록 안 된 사람의 푸시는 무시 (워크플로는 실패시키지 않음)
  }
  const memberName =
    member.properties[PROP.teamName]?.title?.[0]?.plain_text || login;

  const blocks = commitBlocks(commits, branch);
  const existing = await findLog(dateStr, member.id);

  if (existing) {
    // 기존 행에 커밋 누적
    await notion.blocks.children.append({
      block_id: existing.id,
      children: blocks,
    });
    const prev = existing.properties[PROP.logCount]?.number || 0;
    await notion.pages.update({
      page_id: existing.id,
      properties: { [PROP.logCount]: { number: prev + commits.length } },
    });
    console.log(`누적 완료: ${dateStr} / ${memberName} (+${commits.length})`);
  } else {
    // 새 행 생성
    await notion.pages.create({
      parent: { database_id: WORKLOG_DB },
      properties: {
        [PROP.logTitle]: {
          title: [{ text: { content: `${dateStr} ${memberName}` } }],
        },
        [PROP.logDate]: { date: { start: dateStr } },
        [PROP.logMember]: { relation: [{ id: member.id }] },
        [PROP.logCount]: { number: commits.length },
      },
      children: blocks,
    });
    console.log(`신규 생성: ${dateStr} / ${memberName} (${commits.length})`);
  }
}

main().catch((e) => {
  console.error("Notion 기록 실패:", e.body || e.message || e);
  process.exit(1);
});
