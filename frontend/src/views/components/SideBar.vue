<template>
  <el-aside width="200px">
    <el-col class="sidebar">
      <el-header class="sidebar-header"> <span>MI WEBSITE</span> </el-header>
      <el-row class="menu-sidebar">
        <el-menu
          default-active="1"
          class="el-menu-vertical"
          background-color="#474885"
          text-color="#fff"
          active-text-color="#00BCD4"
          @select="handleMenu"
        >
          <el-menu-item
            v-for="sprint in sprints"
            :key="sprint.id"
            :index="sprint.id"
          >
            <i :class="sprint.icon"></i>
            <span>{{ capitalize(sprint.name) }}</span>
          </el-menu-item>
        </el-menu>
      </el-row>
    </el-col>
  </el-aside>
</template>

<script>
export default {
  name: 'SideBar',
  props: ['sprints'],
  methods: {
    capitalize(word) {
      return _.capitalize(word);
    },
    handleMenu(index) {
      this.$emit('changeSprint', index);
      let sprint = this.sprints.find(x => x.id == index);
      console.log(sprint);
      this.$router.push({
        name: sprint.routeName,
        params: { sprint: sprint.name },
      });
    },
  },
};
</script>

<style scoped>
.el-menu-vertical,
.menu-sidebar,
.sidebar {
  height: 100%;
}

aside {
  overflow: hidden;
}

.sidebar-header {
  background-color: #673ab7;
  color: #ffffff;
  text-align: center;
  line-height: 65px;
}
</style>
