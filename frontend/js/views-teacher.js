/* ============ 教师端 ============ */
const TeacherView = {
  emits: ['logout'],
  data() {
    return {
      user: Utils.getUser(),
      activeTab: 'courses',
      courses: [],
      /* 题库管理 */
      questionCourseId: null,
      questions: [],
      qDialog: false,
      qForm: { id: null, courseId: null, title: '', a: '', b: '', c: '', d: '', answer: 'A' },
      /* 试卷管理 */
      papers: [],
      currentPaper: null,       // 当前查看的试卷
      paperQuestions: [],       // 试卷题目 PaperQuestionVO
      addPqDialog: false,
      pqForm: { questionId: null, score: 5 },
      /* 成绩管理 */
      scoreCourseId: null,
      scoreList: [],
      queryUserId: null,
      queryCourseId: null,
      queryResult: [],
      /* 用户管理 */
      users: [],
      searchName: '',
      uDialog: false,
      uForm: { id: null, name: '', password: '', sex: '男', phone: '', number: '', college: '', identity: 'student' }
    };
  },
  template: `
  <el-container class="layout">
    <el-aside width="200px" class="aside">
      <div class="logo">在线考试系统</div>
      <el-menu :default-active="activeTab" @select="t => activeTab = t">
        <el-menu-item index="courses"><el-icon><notebook/></el-icon>我的课程</el-menu-item>
        <el-menu-item index="questions"><el-icon><collection/></el-icon>题库管理</el-menu-item>
        <el-menu-item index="papers"><el-icon><document/></el-icon>试卷管理</el-menu-item>
        <el-menu-item index="scores"><el-icon><data-analysis/></el-icon>成绩管理</el-menu-item>
        <el-menu-item index="users"><el-icon><user-filled/></el-icon>用户管理</el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <span>教师端</span>
        <span>
          <el-tag type="warning" style="margin-right:12px">{{ user.name }}</el-tag>
          <el-button link type="danger" @click="$emit('logout')">退出登录</el-button>
        </span>
      </el-header>
      <el-main class="main">

        <!-- 我的课程 -->
        <div v-if="activeTab==='courses'">
          <el-table :data="courses" border>
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="courseName" label="课程名称" />
            <el-table-column label="操作" width="220">
              <template #default="s">
                <el-button type="primary" link @click="goQuestions(s.row)">管理题目</el-button>
                <el-button type="success" link @click="goScores(s.row)">查看成绩</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-empty v-if="!courses.length" description="暂无任教课程" />
        </div>

        <!-- 题库管理 -->
        <div v-if="activeTab==='questions'">
          <el-form inline class="mb16">
            <el-form-item label="课程">
              <el-select v-model="questionCourseId" placeholder="选择课程" style="width:220px" @change="loadQuestions">
                <el-option v-for="c in courses" :key="c.id" :label="c.courseName" :value="c.id" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="!questionCourseId" @click="openQuestionDialog()">新增题目</el-button>
            </el-form-item>
          </el-form>
          <el-table :data="questions" border>
            <el-table-column prop="id" label="ID" width="70" />
            <el-table-column prop="title" label="题干" min-width="220" show-overflow-tooltip />
            <el-table-column prop="a" label="选项A" show-overflow-tooltip />
            <el-table-column prop="b" label="选项B" show-overflow-tooltip />
            <el-table-column prop="c" label="选项C" show-overflow-tooltip />
            <el-table-column prop="d" label="选项D" show-overflow-tooltip />
            <el-table-column prop="answer" label="答案" width="70" align="center" />
            <el-table-column label="操作" width="140">
              <template #default="s">
                <el-button type="primary" link @click="openQuestionDialog(s.row)">编辑</el-button>
                <el-button type="danger" link @click="removeQuestion(s.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-empty v-if="questionCourseId && !questions.length" description="该课程暂无题目" />

          <el-dialog v-model="qDialog" :title="qForm.id ? '编辑题目' : '新增题目'" width="560px">
            <el-form label-width="70px">
              <el-form-item label="课程">
                <el-select v-model="qForm.courseId" style="width:100%" :disabled="!!qForm.id">
                  <el-option v-for="c in courses" :key="c.id" :label="c.courseName" :value="c.id" />
                </el-select>
              </el-form-item>
              <el-form-item label="题干"><el-input v-model="qForm.title" type="textarea" :rows="2" /></el-form-item>
              <el-form-item label="选项A"><el-input v-model="qForm.a" /></el-form-item>
              <el-form-item label="选项B"><el-input v-model="qForm.b" /></el-form-item>
              <el-form-item label="选项C"><el-input v-model="qForm.c" /></el-form-item>
              <el-form-item label="选项D"><el-input v-model="qForm.d" /></el-form-item>
              <el-form-item label="答案">
                <el-radio-group v-model="qForm.answer">
                  <el-radio value="A">A</el-radio><el-radio value="B">B</el-radio>
                  <el-radio value="C">C</el-radio><el-radio value="D">D</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-form>
            <template #footer>
              <el-button @click="qDialog = false">取消</el-button>
              <el-button type="primary" @click="saveQuestion">保存</el-button>
            </template>
          </el-dialog>
        </div>

        <!-- 试卷管理 -->
        <div v-if="activeTab==='papers'">
          <el-table :data="papers" border>
            <el-table-column prop="id" label="ID" width="70" />
            <el-table-column prop="paperName" label="试卷名称" />
            <el-table-column prop="totalScore" label="总分" width="90" align="center" />
            <el-table-column prop="duration" label="时长(分钟)" width="110" align="center" />
            <el-table-column label="课程" width="140">
              <template #default="s">{{ courseName(s.row.courseId) }}</template>
            </el-table-column>
            <el-table-column label="操作" width="120">
              <template #default="s">
                <el-button type="primary" link @click="openPaper(s.row)">查看组题</el-button>
              </template>
            </el-table-column>
          </el-table>

          <template v-if="currentPaper">
            <el-divider />
            <h3>《{{ currentPaper.paperName }}》题目明细</h3>
            <el-button type="primary" size="small" class="mb16" @click="openAddPq">向本卷添加题目</el-button>
            <el-table :data="paperQuestions" border>
              <el-table-column prop="title" label="题干" min-width="240" show-overflow-tooltip />
              <el-table-column prop="score" label="分值" width="80" align="center" />
              <el-table-column prop="answer" label="答案" width="80" align="center" />
            </el-table>
          </template>

          <el-dialog v-model="addPqDialog" title="添加题目到试卷" width="520px">
            <el-form label-width="70px">
              <el-form-item label="题目">
                <el-select v-model="pqForm.questionId" filterable style="width:100%" placeholder="选择本课程题库中的题目">
                  <el-option v-for="q in questions" :key="q.id" :label="q.title" :value="q.id" />
                </el-select>
              </el-form-item>
              <el-form-item label="分值">
                <el-input-number v-model="pqForm.score" :min="1" :max="100" />
              </el-form-item>
            </el-form>
            <template #footer>
              <el-button @click="addPqDialog = false">取消</el-button>
              <el-button type="primary" @click="savePq">添加</el-button>
            </template>
          </el-dialog>
        </div>

        <!-- 成绩管理 -->
        <div v-if="activeTab==='scores'">
          <el-form inline class="mb16">
            <el-form-item label="课程">
              <el-select v-model="scoreCourseId" placeholder="选择课程" style="width:220px" @change="loadCourseScore">
                <el-option v-for="c in courses" :key="c.id" :label="c.courseName" :value="c.id" />
              </el-select>
            </el-form-item>
          </el-form>
          <el-table :data="scoreList" border class="mb16">
            <el-table-column prop="userName" label="学生" width="120" />
            <el-table-column prop="studentNo" label="学号" width="140" />
            <el-table-column prop="paperName" label="试卷" />
            <el-table-column label="成绩" width="110" align="center">
              <template #default="s">{{ s.row.userScore }} / {{ s.row.totalScore }}</template>
            </el-table-column>
            <el-table-column label="考试时间" width="180">
              <template #default="s">{{ Utils.fmtTime(s.row.examTime) }}</template>
            </el-table-column>
          </el-table>
          <el-empty v-if="scoreCourseId && !scoreList.length" description="该课程暂无成绩" />

          <el-divider />
          <h3>查询学生某课程历次成绩</h3>
          <el-form inline class="mb16">
            <el-form-item label="学生ID"><el-input-number v-model="queryUserId" :min="1" controls-position="right" /></el-form-item>
            <el-form-item label="课程">
              <el-select v-model="queryCourseId" style="width:200px" placeholder="选择课程">
                <el-option v-for="c in courses" :key="c.id" :label="c.courseName" :value="c.id" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="!queryUserId || !queryCourseId" @click="loadStudentScore">查询</el-button>
            </el-form-item>
          </el-form>
          <el-table v-if="queryResult.length" :data="queryResult" border>
            <el-table-column prop="paperName" label="试卷" />
            <el-table-column label="成绩" width="110" align="center">
              <template #default="s">{{ s.row.userScore }} / {{ s.row.totalScore }}</template>
            </el-table-column>
            <el-table-column label="考试时间" width="180">
              <template #default="s">{{ Utils.fmtTime(s.row.examTime) }}</template>
            </el-table-column>
          </el-table>
        </div>

        <!-- 用户管理 -->
        <div v-if="activeTab==='users'">
          <el-form inline class="mb16">
            <el-form-item label="按姓名">
              <el-input v-model="searchName" placeholder="输入姓名" clearable style="width:180px" @keyup.enter="searchUser" />
            </el-form-item>
            <el-form-item>
              <el-button @click="searchUser">搜索</el-button>
              <el-button @click="loadUsers">刷新全部</el-button>
              <el-button type="primary" @click="openUserDialog()">新增用户</el-button>
            </el-form-item>
          </el-form>
          <el-table :data="users" border>
            <el-table-column prop="id" label="ID" width="70" />
            <el-table-column prop="name" label="姓名" width="110" />
            <el-table-column prop="number" label="学号/工号" width="150" />
            <el-table-column prop="sex" label="性别" width="70" />
            <el-table-column prop="phone" label="电话" width="140" />
            <el-table-column prop="college" label="学院" />
            <el-table-column label="身份" width="90">
              <template #default="s">
                <el-tag :type="s.row.identity === 'teacher' || s.row.identity === '教师' ? 'warning' : 'primary'">
                  {{ s.row.identity === 'teacher' ? '教师' : s.row.identity === 'student' ? '学生' : s.row.identity }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="140">
              <template #default="s">
                <el-button type="primary" link @click="openUserDialog(s.row)">编辑</el-button>
                <el-button type="danger" link @click="removeUser(s.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-dialog v-model="uDialog" :title="uForm.id ? '编辑用户' : '新增用户'" width="520px">
            <el-form label-width="80px">
              <el-form-item label="姓名"><el-input v-model="uForm.name" /></el-form-item>
              <el-form-item label="密码">
                <el-input v-model="uForm.password" type="password" show-password :placeholder="uForm.id ? '留空则不修改' : ''" />
              </el-form-item>
              <el-form-item label="学号/工号"><el-input v-model="uForm.number" /></el-form-item>
              <el-form-item label="性别">
                <el-radio-group v-model="uForm.sex">
                  <el-radio value="男">男</el-radio><el-radio value="女">女</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="电话"><el-input v-model="uForm.phone" /></el-form-item>
              <el-form-item label="学院"><el-input v-model="uForm.college" /></el-form-item>
              <el-form-item label="身份">
                <el-radio-group v-model="uForm.identity" :disabled="!!uForm.id">
                  <el-radio value="student">学生</el-radio>
                  <el-radio value="teacher">教师</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-form>
            <template #footer>
              <el-button @click="uDialog = false">取消</el-button>
              <el-button type="primary" @click="saveUser">保存</el-button>
            </template>
          </el-dialog>
        </div>

      </el-main>
    </el-container>
  </el-container>`,
  computed: { Utils() { return Utils; } },
  async mounted() {
    try {
      const res = await API.teacherMyCourses(this.user.id);
      this.courses = res.data || [];
    } catch (e) { Utils.toastError(e); }
    this.loadPapers();
    this.loadUsers();
  },
  methods: {
    courseName(courseId) {
      const c = this.courses.find(x => x.id === courseId);
      return c ? c.courseName : courseId;
    },

    /* ---------- 题库 ---------- */
    goQuestions(course) {
      this.questionCourseId = course.id;
      this.activeTab = 'questions';
      this.loadQuestions();
    },
    async loadQuestions() {
      if (!this.questionCourseId) return;
      try {
        const res = await API.questionByCourse(this.questionCourseId);
        this.questions = res.data || [];
      } catch (e) { Utils.toastError(e); }
    },
    openQuestionDialog(row) {
      this.qForm = row
        ? { ...row }
        : { id: null, courseId: this.questionCourseId, title: '', a: '', b: '', c: '', d: '', answer: 'A' };
      this.qDialog = true;
    },
    async saveQuestion() {
      if (!this.qForm.title) return ElementPlus.ElMessage.warning('请输入题干');
      try {
        if (this.qForm.id) await API.questionUpdate(this.qForm);
        else await API.questionAdd(this.qForm);
        ElementPlus.ElMessage.success('保存成功');
        this.qDialog = false;
        this.loadQuestions();
      } catch (e) { Utils.toastError(e); }
    },
    async removeQuestion(row) {
      try {
        await ElementPlus.ElMessageBox.confirm(`确定删除题目「${row.title}」？`, '删除确认', { type: 'warning' });
      } catch (e) { return; }
      try {
        await API.questionDelete(row.id);
        ElementPlus.ElMessage.success('删除成功');
        this.loadQuestions();
      } catch (e) { Utils.toastError(e); }
    },

    /* ---------- 试卷 ---------- */
    async loadPapers() {
      try {
        const res = await API.paperList();
        this.papers = res.data || [];
      } catch (e) { Utils.toastError(e); }
    },
    async openPaper(paper) {
      this.currentPaper = paper;
      try {
        const res = await API.paperQuestionTeacher(paper.id);
        this.paperQuestions = res.data || [];
        // 组题时需要本课程的题库
        this.questionCourseId = paper.courseId;
        await this.loadQuestions();
      } catch (e) { Utils.toastError(e); }
    },
    openAddPq() {
      this.pqForm = { questionId: null, score: 5 };
      this.addPqDialog = true;
    },
    async savePq() {
      if (!this.pqForm.questionId) return ElementPlus.ElMessage.warning('请选择题目');
      try {
        await API.paperQuestionAdd({
          paperId: this.currentPaper.id,
          questionId: this.pqForm.questionId,
          score: this.pqForm.score
        });
        ElementPlus.ElMessage.success('添加成功');
        this.addPqDialog = false;
        this.openPaper(this.currentPaper);
      } catch (e) { Utils.toastError(e); }
    },

    /* ---------- 成绩 ---------- */
    goScores(course) {
      this.scoreCourseId = course.id;
      this.activeTab = 'scores';
      this.loadCourseScore();
    },
    async loadCourseScore() {
      if (!this.scoreCourseId) return;
      try {
        const res = await API.courseScoreList(this.scoreCourseId);
        this.scoreList = res.data || [];
      } catch (e) { Utils.toastError(e); }
    },
    async loadStudentScore() {
      try {
        const res = await API.studentCourseScore(this.queryUserId, this.queryCourseId);
        this.queryResult = res.data || [];
        if (!this.queryResult.length) ElementPlus.ElMessage.info('该学生此课程暂无成绩');
      } catch (e) { Utils.toastError(e); }
    },

    /* ---------- 用户 ---------- */
    async loadUsers() {
      try {
        const res = await API.userList();
        this.users = res.data || [];
      } catch (e) { Utils.toastError(e); }
    },
    async searchUser() {
      if (!this.searchName) return this.loadUsers();
      try {
        const res = await API.userFindByName(this.searchName);
        this.users = res.data ? [res.data] : [];
        if (!res.data) ElementPlus.ElMessage.info('未找到该用户');
      } catch (e) { Utils.toastError(e); }
    },
    openUserDialog(row) {
      this.uForm = row
        ? { ...row, password: '' }
        : { id: null, name: '', password: '', sex: '男', phone: '', number: '', college: '', identity: 'student' };
      this.uDialog = true;
    },
    async saveUser() {
      if (!this.uForm.name) return ElementPlus.ElMessage.warning('请输入姓名');
      if (!this.uForm.id && !this.uForm.password) return ElementPlus.ElMessage.warning('新用户请设置密码');
      try {
        if (this.uForm.id) {
          await API.userUpdate({ ...this.uForm, password: this.uForm.password || null });
        } else {
          await API.userAdd(this.uForm);
        }
        ElementPlus.ElMessage.success('保存成功');
        this.uDialog = false;
        this.loadUsers();
      } catch (e) { Utils.toastError(e); }
    },
    async removeUser(row) {
      try {
        await ElementPlus.ElMessageBox.confirm(`确定删除用户「${row.name}」？`, '删除确认', { type: 'warning' });
      } catch (e) { return; }
      try {
        await API.userDelete(row.id);
        ElementPlus.ElMessage.success('删除成功');
        this.loadUsers();
      } catch (e) { Utils.toastError(e); }
    }
  }
};

window.TeacherView = TeacherView;
