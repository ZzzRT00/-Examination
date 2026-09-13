/* ============ 根组件：按登录状态切换 登录/学生端/教师端 ============ */
const App = {
  components: {
    'login-view': window.LoginView,
    'student-view': window.StudentView,
    'teacher-view': window.TeacherView
  },
  data() {
    return {
      user: Utils.getUser(),
      token: localStorage.getItem('token')
    };
  },
  computed: {
    currentView() {
      if (!this.token || !this.user) return 'login-view';
      return Utils.isTeacher(this.user) ? 'teacher-view' : 'student-view';
    }
  },
  template: `<component :is="currentView" @logged-in="onLoggedIn" @logout="onLogout" />`,
  methods: {
    onLoggedIn(user) {
      this.user = user;
      this.token = localStorage.getItem('token');
      ElementPlus.ElMessage.success('登录成功，欢迎 ' + user.name);
    },
    onLogout() {
      localStorage.removeItem('token');
      localStorage.removeItem('userInfo');
      this.token = null;
      this.user = null;
      ElementPlus.ElMessage.success('已退出登录');
    }
  }
};

const app = Vue.createApp(App);
app.use(ElementPlus, window.ElementPlusLocaleZhCn ? { locale: window.ElementPlusLocaleZhCn } : {});
if (window.ElementPlusIconsVue) {
  for (const [name, comp] of Object.entries(window.ElementPlusIconsVue)) {
    app.component(name, comp);
  }
}
app.mount('#app');
