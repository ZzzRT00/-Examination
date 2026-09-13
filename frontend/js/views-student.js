/* ============ 登录页 ============ */
const LoginView = {
  emits: ['logged-in'],
  data() {
    return {
      form: { number: '', password: '', identity: '学生' },
      loading: false
    };
  },
  template: `
  <div class="login-wrap">
    <el-card class="login-card">
      <h2 class="login-title">在线考试系统</h2>
      <el-form label-width="60px" size="large">
        <el-form-item label="学工号">
          <el-input v-model="form.number" placeholder="请输入学工号" @keyup.enter="doLogin" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" show-password placeholder="请输入密码" @keyup.enter="doLogin" />
        </el-form-item>
        <el-form-item label="身份">
          <el-radio-group v-model="form.identity">
            <el-radio value="学生">学生</el-radio>
            <el-radio value="教师">教师</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-button type="primary" style="width:100%" :loading="loading" @click="doLogin">登 录</el-button>
      </el-form>
    </el-card>
  </div>`,
  methods: {
    async doLogin() {
      if (!this.form.number || !this.form.password) {
        return ElementPlus.ElMessage.warning('请输入学工号和密码');
      }
      this.loading = true;
      try {
        const res = await API.login(this.form);
        localStorage.setItem('token', res.data.token);
        Utils.setUser(res.data.userInfo);
        this.$emit('logged-in', res.data.userInfo);
      } catch (e) { Utils.toastError(e); }
      finally { this.loading = false; }
    }
  }
};

/* ============ 学生端 ============ */
const StudentView = {
  emits: ['logout'],
  data() {
    return {
      user: Utils.getUser(),
      activeTab: 'courses',
      // 我的课程
      courses: [],
      courseScores: {},          // courseId -> 最新成绩 ScoreVO
      // 在线考试
      exam: {
        courseId: null, papers: [], paperId: null,
        paper: null, questions: [], answers: {},
        left: 0, timer: null, submitting: false, score: null
      },
      // 个人信息
      profile: {}, profilePassword: ''
    };
  },
  template: `
  <el-container class="layout">
    <el-aside width="200px" class="aside">
      <div class="logo">在线考试系统</div>
      <el-menu :default-active="activeTab" @select="onTab">
        <el-menu-item index="courses"><el-icon><menu-icon/></el-icon>我的课程</el-menu-item>
        <el-menu-item index="exam"><el-icon><edit-pen/></el-icon>在线考试</el-menu-item>
        <el-menu-item index="scores"><el-icon><trophy/></el-icon>我的成绩</el-menu-item>
        <el-menu-item index="profile"><el-icon><user/></el-icon>个人信息</el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <span>学生端</span>
        <span>
          <el-tag type="primary" style="margin-right:12px">{{ user.name }}</el-tag>
          <el-button link type="danger" @click="$emit('logout')">退出登录</el-button>
        </span>
      </el-header>
      <el-main class="main">

        <!-- 我的课程 -->
        <div v-if="activeTab==='courses'">
          <el-row :gutter="16">
            <el-col :span="8" v-for="c in courses" :key="c.id">
              <el-card shadow="hover" class="course-card">
                <h3>{{ c.courseName }}</h3>
                <p class="muted">
                  <template v-if="courseScores[c.id]">
                    最新成绩：{{ courseScores[c.id].userScore }} / {{ courseScores[c.id].totalScore }}
                    （{{ Utils.fmtTime(courseScores[c.id].examTime) }}）
                  </template>
                  <template v-else>暂未考试</template>
                </p>
                <el-button v-if="courseScores[c.id]" type="warning" @click="startExam(c)">重考</el-button>
                <el-button v-else type="primary" @click="startExam(c)">进入考试</el-button>
              </el-card>
            </el-col>
          </el-row>
          <el-empty v-if="!courses.length" description="暂无所选课程" />
        </div>

        <!-- 在线考试 -->
        <div v-if="activeTab==='exam'">
          <el-card v-if="!exam.paper" class="mb16">
            <el-form inline>
              <el-form-item label="课程">
                <el-select v-model="exam.courseId" placeholder="选择课程" style="width:220px" @change="loadPapers">
                  <el-option v-for="c in courses" :key="c.id" :label="c.courseName" :value="c.id" />
                </el-select>
              </el-form-item>
              <el-form-item label="试卷">
                <el-select v-model="exam.paperId" placeholder="选择试卷" style="width:260px" :disabled="!exam.papers.length" @change="loadPaper">
                  <el-option v-for="p in exam.papers" :key="p.id" :label="p.paperName" :value="p.id" />
                </el-select>
              </el-form-item>
              <el-form-item>
                <span class="muted" v-if="exam.courseId && !exam.papers.length">该课程暂无试卷</span>
              </el-form-item>
            </el-form>
          </el-card>

          <template v-else>
            <el-card class="mb16 exam-head">
              <span class="exam-name">{{ exam.paper.paperName }}</span>
              <span class="muted" style="margin-left:16px">总分 {{ exam.paper.totalScore }} 分</span>
              <span class="countdown" :class="{danger: exam.left <= 60}">剩余 {{ fmtLeft }}</span>
            </el-card>
            <el-card v-for="(q, idx) in exam.questions" :key="q.questionId" class="mb16">
              <p class="q-title">{{ idx + 1 }}. {{ q.title }}（{{ q.score }} 分）</p>
              <el-radio-group v-model="exam.answers[q.questionId]" :disabled="exam.score !== null">
                <el-radio value="A" class="opt">A. {{ q.optionA }}</el-radio>
                <el-radio value="B" class="opt">B. {{ q.optionB }}</el-radio>
                <el-radio value="C" class="opt">C. {{ q.optionC }}</el-radio>
                <el-radio value="D" class="opt">D. {{ q.optionD }}</el-radio>
              </el-radio-group>
            </el-card>
            <el-button v-if="exam.score === null" type="success" size="large" :loading="exam.submitting" @click="submitExam">交 卷</el-button>
            <el-result v-else icon="success" title="考试完成" :sub-title="'你的得分：' + exam.score + ' / ' + exam.paper.totalScore">
              <template #extra>
                <el-button type="primary" @click="resetExam">返回选择试卷</el-button>
              </template>
            </el-result>
          </template>
        </div>

        <!-- 我的成绩 -->
        <div v-if="activeTab==='scores'">
          <el-table :data="scoreRows" border>
            <el-table-column prop="courseName" label="课程" />
            <el-table-column prop="paperName" label="试卷" />
            <el-table-column label="成绩">
              <template #default="s">
                <span v-if="s.row.userScore === null" class="muted">未考试</span>
                <template v-else>{{ s.row.userScore }} / {{ s.row.totalScore }}</template>
              </template>
            </el-table-column>
            <el-table-column label="考试时间">
              <template #default="s">{{ Utils.fmtTime(s.row.examTime) }}</template>
            </el-table-column>
          </el-table>
        </div>

        <!-- 个人信息 -->
        <div v-if="activeTab==='profile'">
          <el-card style="max-width:560px">
            <el-form label-width="80px">
              <el-form-item label="学号"><el-input v-model="profile.number" disabled /></el-form-item>
              <el-form-item label="姓名"><el-input v-model="profile.name" /></el-form-item>
              <el-form-item label="性别">
                <el-radio-group v-model="profile.sex">
                  <el-radio value="男">男</el-radio>
                  <el-radio value="女">女</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="电话"><el-input v-model="profile.phone" /></el-form-item>
              <el-form-item label="学院"><el-input v-model="profile.college" /></el-form-item>
              <el-form-item label="新密码">
                <el-input v-model="profilePassword" type="password" show-password placeholder="留空则不修改密码" />
              </el-form-item>
              <el-button type="primary" @click="saveProfile">保存修改</el-button>
            </el-form>
          </el-card>
        </div>

      </el-main>
    </el-container>
  </el-container>`,
  computed: {
    Utils() { return Utils; },
    fmtLeft() {
      const m = Math.floor(this.exam.left / 60), s = this.exam.left % 60;
      return `${String(m).padStart(2, '0')}:${String(s).padStart(2, '0')}`;
    },
    scoreRows() {
      return this.courses.map(c => this.courseScores[c.id] || {
        courseName: c.courseName, paperName: '-', userScore: null, totalScore: '-', examTime: null
      });
    }
  },
  async mounted() {
    await this.loadCourses();
    this.profile = { ...this.user };
    // 恢复上次未完成的考试状态不做持久化，简单处理
    if (!this.courses.length) return;
    this.exam.courseId = this.courses[0].id;
    await this.loadPapers();
  },
  beforeUnmount() { this.stopTimer(); },
  methods: {
    onTab(tab) {
      if (this.activeTab === 'exam' && tab !== 'exam' && this.exam.paper && this.exam.score === null) {
        return ElementPlus.ElMessageBox.confirm('考试正在进行中，离开将不保存答题记录，确定离开？', '提示', { type: 'warning' })
          .then(() => { this.stopTimer(); this.activeTab = tab; })
          .catch(() => {});
      }
      this.activeTab = tab;
    },
    async loadCourses() {
      try {
        const res = await API.studentMyCourses(this.user.id);
        this.courses = res.data || [];
        const tasks = this.courses.map(c =>
          API.studentLatestScore(this.user.id, c.id)
            .then(r => { if (r.data) this.courseScores[c.id] = r.data; })
        );
        await Promise.all(tasks);
      } catch (e) { Utils.toastError(e); }
    },

    /* ---------- 考试 ---------- */
    startExam(course) {
      this.exam.courseId = course.id;
      this.activeTab = 'exam';
      this.loadPapers();
    },
    async loadPapers() {
      this.exam.papers = []; this.exam.paperId = null;
      if (!this.exam.courseId) return;
      try {
        const res = await API.paperByCourse(this.exam.courseId);
        this.exam.papers = res.data || [];
      } catch (e) { Utils.toastError(e); }
    },
    async loadPaper(paperId) {
      try {
        const [paperRes, questionRes] = await Promise.all([
          API.paperById(paperId),
          API.paperQuestionStudent(paperId)
        ]);
        this.exam.paper = paperRes.data;
        this.exam.questions = questionRes.data || [];
        this.exam.answers = {};
        this.exam.score = null;
        this.exam.left = (this.exam.paper.duration || 60) * 60;
        this.startTimer();
      } catch (e) { Utils.toastError(e); }
    },
    startTimer() {
      this.stopTimer();
      this.exam.timer = setInterval(() => {
        if (this.exam.left > 0) {
          this.exam.left--;
          if (this.exam.left === 0) {
            ElementPlus.ElMessage.warning('时间到，自动交卷');
            this.submitExam();
          }
        }
      }, 1000);
    },
    stopTimer() {
      if (this.exam.timer) { clearInterval(this.exam.timer); this.exam.timer = null; }
    },
    async submitExam() {
      const unanswered = this.exam.questions.filter(q => !this.exam.answers[q.questionId]);
      try {
        await ElementPlus.ElMessageBox.confirm(
          unanswered.length ? `还有 ${unanswered.length} 题未作答，确定交卷？` : '确定交卷吗？',
          '交卷确认', { type: 'warning' }
        );
      } catch (e) { return; }
      this.exam.submitting = true;
      try {
        const res = await API.examSubmit({
          userId: this.user.id,
          paperId: this.exam.paperId,
          // 当前课程已考过则视为重考，后端据此放行重复提交
          retake: !!this.courseScores[this.exam.courseId],
          answerList: this.exam.questions.map(q => ({
            questionId: q.questionId,
            userAnswer: this.exam.answers[q.questionId] || ''
          }))
        });
        this.stopTimer();
        this.exam.score = res.data;
        ElementPlus.ElMessage.success('交卷成功');
        this.loadCourses(); // 刷新课程卡片上的成绩
      } catch (e) { Utils.toastError(e); }
      finally { this.exam.submitting = false; }
    },
    resetExam() {
      this.stopTimer();
      this.exam.paper = null; this.exam.paperId = null;
      this.exam.questions = []; this.exam.answers = {}; this.exam.score = null;
    },

    /* ---------- 个人信息 ---------- */
    async saveProfile() {
      try {
        await API.userUpdate({
          id: this.profile.id,
          name: this.profile.name,
          sex: this.profile.sex,
          phone: this.profile.phone,
          college: this.profile.college,
          password: this.profilePassword || null
        });
        ElementPlus.ElMessage.success('保存成功');
        if (this.profilePassword) this.profilePassword = '';
        const updated = { ...this.user, ...this.profile };
        Utils.setUser(updated);
        this.user = updated;
      } catch (e) { Utils.toastError(e); }
    }
  }
};

window.LoginView = LoginView;
window.StudentView = StudentView;
