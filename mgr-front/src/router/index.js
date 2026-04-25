import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/customer'
  },
  {
    path: '/',
    component: () => import('../layouts/MainLayout.vue'),
    children: [
      {
        path: 'customer',
        name: 'Customer',
        component: () => import('../views/customer/CustomerList.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: 'driver',
        name: 'Driver',
        component: () => import('../views/driver/DriverList.vue'),
        meta: { title: '司机管理' }
      },
      {
        path: 'coupon',
        name: 'Coupon',
        component: () => import('../views/coupon/CouponList.vue'),
        meta: { title: '优惠券管理' }
      },
      {
        path: 'order',
        name: 'Order',
        component: () => import('../views/order/OrderList.vue'),
        meta: { title: '订单管理' }
      },
      {
        path: 'log',
        name: 'Log',
        component: () => import('../views/log/LogList.vue'),
        meta: { title: '日志管理' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
