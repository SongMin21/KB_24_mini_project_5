import { createRouter, createWebHistory } from 'vue-router';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/thinking/list',
    },
    {
      path: '/thinking/list',
      name: 'thinking-list',
      component: () => import('../views/ThinkingListView.vue'),
    },
    {
      path: '/thinking/create',
      name: 'thinking-create',
      component: () => import('../views/ThinkingCreateView.vue'),
    },
    {
      path: '/thinking/update/:id',
      name: 'thinking-update',
      component: () => import('../views/ThinkingUpdateView.vue'),
      props: true,
    },
    {
      path: '/thinking/:id',
      name: 'thinking-detail',
      component: () => import('../views/ThinkingDetailView.vue'),
      props: true,
    },
    {
      path: '/error',
      name: 'error',
      component: () => import('../views/ErrorView.vue'),
      props: (route) => ({
        status: Number(route.query.status) || 500,
        message: route.query.message,
      }),
    },
    {
      // catch-all은 반드시 배열의 마지막에 위치해야 함
      path: '/:pathMatch(.*)*',
      name: 'not-found',
      component: () => import('../views/ErrorView.vue'),
      props: { status: 404 },
    },
  ],
});

export default router;
