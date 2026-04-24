# 硅谷代驾管理系统前端

基于 Vue 3 + Vite + Element Plus 开发的管理后台系统。

## 技术栈

- Vue 3 (Composition API)
- Vite
- Element Plus
- Vue Router 4
- Pinia
- Axios

## 项目结构

```
mgr-front/
├── src/
│   ├── api/              # API 接口
│   │   ├── customer.js   # 用户相关接口
│   │   ├── driver.js     # 司机相关接口
│   │   ├── coupon.js     # 优惠券相关接口
│   │   └── order.js      # 订单相关接口
│   ├── assets/           # 静态资源
│   │   └── styles/       # 样式文件
│   ├── layouts/          # 布局组件
│   │   └── MainLayout.vue # 主布局
│   ├── router/           # 路由配置
│   │   └── index.js
│   ├── utils/            # 工具类
│   │   └── request.js    # Axios 封装
│   ├── views/            # 页面组件
│   │   ├── customer/     # 用户管理
│   │   ├── driver/       # 司机管理
│   │   ├── coupon/       # 优惠券管理
│   │   └── order/        # 订单管理
│   ├── App.vue
│   └── main.js
├── index.html
├── package.json
└── vite.config.js
```

## 功能模块

### 1. 用户管理
- 用户列表分页查询
- 按昵称搜索用户
- 启用/禁用用户

### 2. 司机管理
- 司机列表分页查询
- 按姓名搜索司机
- 按认证状态筛选司机
- 启用/禁用司机

### 3. 优惠券管理
- 优惠券列表分页查询
- 按名称搜索优惠券
- 按状态筛选优惠券
- 新增优惠券（前端已实现，后端 API 待补充）
- 编辑优惠券
- 删除优惠券

### 4. 订单管理
- 订单列表分页查询
- 按订单号搜索订单
- 按乘客 ID 查询订单
- 按司机 ID 查询订单
- 按订单状态筛选订单
- 查看订单详情

## 开发指南

### 安装依赖

```bash
npm install
```

### 启动开发服务器

```bash
npm run dev
```

访问 http://localhost:5173

### 构建生产版本

```bash
npm run build
```

## 配置说明

### API 地址配置

在 `src/utils/request.js` 中修改 `baseURL`：

```javascript
const service = axios.create({
  baseURL: 'http://localhost:8300', // 修改为实际的 API 地址
  timeout: 10000
})
```

## 注意事项

1. 新增优惠券的后端 API 暂未实现，前端已做好 UI 和逻辑，待后端 API 完成后即可正常使用。
2. 删除优惠券的后端 API 暂未实现，前端已做好 UI，待后端 API 完成后即可正常使用。
3. 当前需要确保后端服务 web-mgr 运行在 http://localhost:8300 端口。

## 后续待实现

- [ ] 新增优惠券后端 API
- [ ] 删除优惠券后端 API
- [ ] 编辑优惠券后端 API
