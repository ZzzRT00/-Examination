/* ============ axios 封装 ============ */
const http = axios.create({
  baseURL: 'http://localhost:3728',
  timeout: 10000
});

// 请求拦截：自动携带 token（后端拦截器读取的是原始 token，无 Bearer 前缀）
http.interceptors.request.use(cfg => {
  const token = localStorage.getItem('token');
  if (token) cfg.headers.Authorization = token;
  return cfg;
});

// 响应拦截：统一拆包 Result，401 时回到登录页
http.interceptors.response.use(
  res => {
    const body = res.data;
    if (body && typeof body.code === 'number' && body.code !== 200) {
      if (body.code === 401) {
        localStorage.removeItem('token');
        localStorage.removeItem('userInfo');
        location.hash = '#/login';
        location.reload();
      }
      return Promise.reject(new Error(body.msg || '操作失败'));
    }
    return body; // 调用方直接拿到 {code,msg,data}
  },
  err => {
    if (err.response && err.response.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('userInfo');
      location.hash = '#/login';
      location.reload();
    }
    return Promise.reject(new Error(
      (err.response && err.response.data && err.response.data.msg) || err.message || '网络错误'
    ));
  }
);

/* ============ 接口定义（与后端 Controller 一一对应） ============ */
const API = {
  // --- 用户 UserController ---
  login: data => http.post('/user/login', data),
  userList: () => http.get('/user/list'),
  userFindByName: name => http.get('/user/find', { params: { name } }),
  userGetById: id => http.get(`/user/${id}`),
  userAdd: data => http.post('/user', data),
  userUpdate: data => http.put('/user/student', data),
  userDelete: id => http.delete(`/user/${id}`),

  // --- 课程 CoursesController ---
  studentMyCourses: userId => http.get(`/course/student/myCourses/${userId}`),
  teacherMyCourses: teacherId => http.get(`/course/teacher/myCourses/${teacherId}`),

  // --- 题目 QuestionController ---
  questionByCourse: courseId => http.get(`/question/course/${courseId}`),
  questionAdd: data => http.post('/question/teacher/add', data),
  questionUpdate: data => http.put('/question/teacher/update', data),
  questionDelete: id => http.delete(`/question/teacher/${id}`),

  // --- 试卷 PaperController ---
  paperList: () => http.get('/paper/list'),
  paperById: id => http.get(`/paper/${id}`),
  paperByCourse: courseId => http.get(`/paper/course/${courseId}`),

  // --- 试卷题目 PaperQuestionController ---
  paperQuestionAdd: data => http.post('/paperQuestion/add', data),
  paperQuestionList: paperId => http.get(`/paperQuestion/list/${paperId}`),
  paperQuestionTeacher: paperId => http.get(`/paperQuestion/teacher/paper/${paperId}`),
  paperQuestionStudent: paperId => http.get(`/paperQuestion/student/paper/${paperId}`),

  // --- 答题记录 AnswerRecordController ---
  examSubmit: data => http.post('/answerRecord/student/examSubmit', data),
  courseScoreList: courseId => http.get(`/answerRecord/teacher/courseScore/${courseId}`),
  studentCourseScore: (userId, courseId) =>
    http.get('/answerRecord/teacher/studentScore', { params: { userId, courseId } }),
  studentLatestScore: (userId, courseId) =>
    http.get('/answerRecord/student/courseScore', { params: { userId, courseId } })
};

/* ============ 公共工具 ============ */
const Utils = {
  // userInfo 存取
  getUser() {
    try { return JSON.parse(localStorage.getItem('userInfo') || 'null'); }
    catch (e) { return null; }
  },
  setUser(u) { localStorage.setItem('userInfo', JSON.stringify(u)); },

  // 身份归一化：兼容数据库存中文或英文
  isTeacher(u) { const i = (u && u.identity) || ''; return i === 'teacher' || i === '教师'; },

  // "2026-09-10T20:31:05" -> "2026-09-10 20:31:05"
  fmtTime(t) { return t ? String(t).replace('T', ' ').slice(0, 19) : '-'; },

  // 统一错误提示
  toastError(e) {
    if (window.ElementPlus) ElementPlus.ElMessage.error(e.message || '操作失败');
  }
};
