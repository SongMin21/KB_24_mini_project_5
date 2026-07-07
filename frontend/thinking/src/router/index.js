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
  ],
});

export default router;
