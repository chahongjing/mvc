import Vue from 'vue';
import Router from 'vue-router';
import store from '@/store/store';

Vue.use(Router);

// //自动化部署
// const requireComponent = require.context('../page', true, /\.vue$/);
// const routes = requireComponent.keys().map(fileName => {
//   // 获取组件配置
//   const componentConfig = requireComponent(fileName);
//   // 剥去文件名开头的 `./` 和`.vue`结尾的扩展名
//   const componentName = fileName.replace(/^\.\//,'').replace(/\.vue$/,'');
//   // 全局注册组件
//   const component = Vue.component(
//     componentName.replace(/\//,'-'),
//     // 如果这个组件选项是通过 `export default` 导出的，那么就会优先使用 `.default`，否则回退到使用模块的根。
//     componentConfig.default || componentConfig
//   );
//   return {
//     path: '/'+componentName,
//     name: componentName.replace(/\//,'-'),
//     component
//   }
// });


var router = new Router({
  routes: [
    {
      path: '/',
      component: resolve => require(['@/components/layout/headerAndMenu'], resolve),
      children:[{
        path: '',
        name: 'home',
        component: resolve => require(['@/components/index'], resolve),
        meta: {
            breadcurmbName: '首页',
        }
      }]
    },
    {
      path: '/login',
      name: 'login',
      component: resolve => require(['@/components/user/login'], resolve),
      meta: {
          breadcurmbName: '用户登录',
      }
    },
    {
      path: '/user',
      name: 'user',
      component: resolve => require(['@/components/layout/headerAndMenu'], resolve),
      children: [
        {
          path: 'userList',
          name: 'userList',
          component: resolve => require(['@/components/user/userList'], resolve),
          meta:{
            breadcurmbName: '用户列表',
            pageCode: 'userList_enter'
          }
        },
        {
          path: 'userEdit',
          name: 'userEdit',
          component: resolve => require(['@/components/user/userEdit'], resolve),
          meta:{
            breadcurmbName: '用户详情',
            pageCode: 'userEdit_enter'
          }
        },
        {
          path: 'userRole',
          name: 'userRole',
          component: resolve => require(['@/components/user/userRole'], resolve),
          meta:{
            breadcurmbName: '用户角色',
            pageCode: 'userRole_enter'
          }
        }
      ]
    },
    {
      path: '/admin',
      name: 'admin',
      component: resolve => require(['@/components/layout/headerAndMenu'], resolve),
      children: [
        {
          path: 'menuList',
          name: 'menuList',
          component: resolve => require(['@/components/admin/menuList'], resolve),
          meta:{
            breadcurmbName: '菜单列表',
            pageCode: 'menuList_enter'
          }
        },
        {
          path: 'menuEdit',
          name: 'menuEdit',
          component: resolve => require(['@/components/admin/menuEdit'], resolve),
          meta:{
            breadcurmbName: '菜单详情',
            pageCode: 'menuEdit_enter'
          }
        },
        {
          path: 'functionList',
          name: 'functionList',
          component: resolve => require(['@/components/admin/functionList'], resolve),
          meta:{
            breadcurmbName: '功能列表',
            pageCode: 'functionList_enter'
          }
        },
        {
          path: 'functionEdit',
          name: 'functionEdit',
          component: resolve => require(['@/components/admin/functionEdit'], resolve),
          meta:{
            breadcurmbName: '功能详情',
            pageCode: 'functionEdit_enter'
          }
        },
        {
          path: 'permissionList',
          name: 'permissionList',
          component: resolve => require(['@/components/admin/permissionList'], resolve),
            meta:{
              breadcurmbName: '权限列表',
              pageCode: 'permissionList_enter'
            }
        },
        {
          path: 'permissionEdit',
          name: 'permissionEdit',
          component: resolve => require(['@/components/admin/permissionEdit'], resolve),
          meta:{
            breadcurmbName: '权限详情',
            pageCode: 'permissionEdit_enter'
          }
        },
        {
          path: 'roleList',
          name: 'roleList',
          component: resolve => require(['@/components/admin/roleList'], resolve),
          meta:{
            breadcurmbName: '角色列表',
            pageCode: 'roleList_enter'
          }
        },
        {
          path: 'roleEdit',
          name: 'roleEdit',
          component: resolve => require(['@/components/admin/roleEdit'], resolve),
          meta:{
            breadcurmbName: '角色详情',
            pageCode: 'roleEdit_enter'
          }
        },
        {
          path: 'configInfoList',
          name: 'configInfoList',
          component: resolve => require(['@/components/admin/configInfoList'], resolve),
          meta:{
            breadcurmbName: '配置列表',
            pageCode: 'configInfoList_enter'
          }
        },
        {
          path: 'configInfoEdit',
          name: 'configInfoEdit',
          component: resolve => require(['@/components/admin/configInfoEdit'], resolve),
          meta:{
            breadcurmbName: '配置详情',
            pageCode: 'configInfoEdit_enter'
          }
        },
        {
          path: 'roleGrantPermission',
          name: 'roleGrantPermission',
          component: resolve => require(['@/components/admin/roleGrantPermission'], resolve),
          meta:{
            breadcurmbName: '角色授权',
            pageCode: 'roleGrantPermission_enter'
          }
        },
        {
          path: 'operLogList',
            name: 'operLogList',
          component: resolve => require(['@/components/admin/operLogList'], resolve),
          meta:{
            breadcurmbName: '日志列表',
              pageCode: 'operLogList_enter'
          }
        },
        {
          path: 'operLogEdit',
            name: 'operLogEdit',
          component: resolve => require(['@/components/admin/operLogEdit'], resolve),
          meta:{
            breadcurmbName: '日志详情',
              pageCode: 'operLogEdit_enter'
          }
        },
      ]
    },
    {
      path:'/sys',
      component: resolve => require(['@/components/layout/headerAndMenu'], resolve),
      children: [
        {
          path: '401',
          name: '401',
          component: resolve => require(['@/components/sys/401'], resolve),
          meta: {
            breadcurmbName: '未授权'
          }
        }
      ]
    },
    {
      path: '/kvConfig',
      name: 'kv',
      component: resolve => require(['@/components/layout/headerAndMenu'], resolve),
      children: [
        {
          path: 'list',
          name: 'kvList',
          component: resolve => require(['@/components/kvConfig/kvList'], resolve),
          meta:{
            breadcurmbName: '键值对列表',
            // pageCode: 'menuList_enter'
          }
        },
        {
          path: 'edit',
          name: 'kvEdit',
          component: resolve => require(['@/components/kvConfig/kvEdit'], resolve),
          meta:{
            breadcurmbName: '键值对编辑',
            // pageCode: 'menuList_enter'
          }
        }
      ]
    },
    {
      path: '/upgradeLog',
      name: 'upgradeLog',
      component: resolve => require(['@/components/layout/headerAndMenu'], resolve),
      children: [
        {
          path: 'list',
          name: 'upgradeLogList',
          component: resolve => require(['@/components/upgradeLog/upgradeLogList'], resolve),
          meta:{
            breadcurmbName: '升级日志列表',
            pageCode: 'upgradeLog_enter'
          }
        },
        {
          path: 'edit',
          name: 'upgradeLogEdit',
          component: resolve => require(['@/components/upgradeLog/upgradeLogEdit'], resolve),
          meta:{
            breadcurmbName: '升级日志编辑',
            pageCode: 'upgradeLogEdit_enter'
          }
        },
        {
          path: 'preview',
          name: 'upgradeLogPreview',
          component: resolve => require(['@/components/upgradeLog/upgradeLogPreview'], resolve),
          meta:{
            breadcurmbName: '升级日志预览',
            pageCode: 'menuList_enter'
          }
        }
      ]
    },
    {
      path: '/switch',
      name: 'switch',
      component: resolve => require(['@/components/layout/headerAndMenu'], resolve),
      children: [
        {
          path: 'list',
          name: 'switchList',
          component: resolve => require(['@/components/switch/list'], resolve),
          meta:{
            breadcurmbName: '开关控制室',
            pageCode: 'switchList_enter'
          }
        }
      ]
    },
    {
      path: '/redis',
      name: 'redis',
      component: resolve => require(['@/components/layout/headerAndMenu'], resolve),
      children: [
        {
          path: '',
          name: 'redisOpt',
          component: resolve => require(['@/components/tools/redis/redis'], resolve),
          meta:{
            breadcurmbName: 'redis操作',
            pageCode: 'redisOpt_enter'
          }
        }
      ]
    },
    {
      path:'/test',
      component: resolve => require(['@/components/layout/headerAndMenu'], resolve),
      children: [
        {
          path: '',
          name: 'test',
          component: resolve => require(['@/components/other/test'], resolve),
          meta: {
            breadcurmbName: '测试'
          }
        }
      ]
    },
    {
      path: '*',
      component: resolve => require(['@/components/layout/headerAndMenu'], resolve),
      children: [
        {
          path: '',
          name: '404',
          component: resolve => require(['@/components/sys/405'], resolve),
          meta: {
              breadcurmbName: '无法访问'
          }
        }
      ]
    }
  ]
});

router.beforeEach(function (to, from, next) {
  if (to.matched.length === 0) {
    next({name: '404'});
    return;
  }
  var user = router.app.$store.state.user;
  var permissionList = router.app.$store.state.permissionList;
  if(Vue.config.devtools) {
    console.log(to);
  }
  if(!user.userId && to && to.path != '/login') {
    next({
     path: 'login',
     query: {redirect: to.fullPath}  // 将跳转的路由path作为参数，登录成功后跳转到该路由
    });
    return;
  }
  // 判断有没有页面权限
  if(to.meta.pageCode) {
    if(!(permissionList && permissionList.some(item => item == to.meta.pageCode))) {
      // router.app.$toaster.error('没有权限！');
      // next(false);
      next({name: '401'});
      return;
    }
  }
  // 不跳转传false
  // next(false);
  // 处理面包屑
  if(to.path != from.path) {
    var breadcrumb = router.app.$store.state.breadcrumb.breadcurmbList;
    var breadcrumbIgnoreUrl = ['/', '/login'];
    var i = 0;
    for(; i < breadcrumb.length; i++) {
      if(breadcrumb[i].path == to.path) {
        breadcrumb.splice(i, breadcrumb.length - i);
        break;
      }
    }
    if(!breadcrumbIgnoreUrl.includes(to.path)) {
      breadcrumb.push({name: to.meta.breadcurmbName || to.name, path:to.path,
        query:to.query,params:to.params});
    }
    router.app.$store.commit("SET_BREADCRUMB", breadcrumb);
  }

  next();
});
export default router;
