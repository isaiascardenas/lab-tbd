<template>
  <el-container class="container">
    <side-bar v-bind:sprints="sprints" @changeSprint="setNavdata"></side-bar>
    <el-container>
      <nav-bar></nav-bar>
      <el-main> <router-view v-bind:navdata="navdata"></router-view> </el-main>
      <el-footer><span class="copyright">© Grupo 4 - 2018</span></el-footer>
    </el-container>
  </el-container>
</template>

<script>
import Main from './components/Main.vue';
import NavBar from './components/NavBar.vue';
import SideBar from './components/SideBar.vue';

export default {
  name: 'Home',
  components: {
    Main,
    NavBar,
    SideBar,
  },
  data() {
    return {
      sprints: [
        {
          id: '1',
          name: 'popularidad',
          routeName: 'Popularidad Deportes',
          icon: 'el-icon-star-on',
        },
        {
          id: '2',
          name: 'influencias',
          routeName: 'Influencias Deportes Usuarios',
          icon: 'el-icon-sort',
        },
        {
          id: '3',
          name: 'distribucion',
          routeName: 'Distribucion Tweets',
          icon: 'el-icon-location',
        },
      ],
      navdata: [
        {
          id: '1',
          name: 'Deportes',
          routeName: 'Popularidad Deportes',
        },
        {
          id: '2',
          name: 'Paises',
          routeName: 'Popularidad Paises',
        },
        {
          id: '3',
          name: 'Fechas',
          routeName: 'Popularidad Fechas',
        },
      ],
    };
  },
  methods: {
    setNavdata(index) {
      if (index == 1) {
        this.navdata = [
          {
            id: '1',
            name: 'Deportes',
            routeName: 'Popularidad Deportes',
          },
          {
            id: '2',
            name: 'Paises',
            routeName: 'Popularidad Paises',
          },
          {
            id: '3',
            name: 'Fechas',
            routeName: 'Popularidad Fechas',
          },
        ];
      } else if (index == 2) {
        this.navdata = [
          {
            id: '1',
            name: 'Influencias Deportes y Usuarios',
            routeName: 'Influencias Deportes Usuarios',
          },
          {
            id: '2',
            name: 'Influencias Paises y Deportes',
            routeName: 'Influencias Paises Deportes',
          },
          {
            id: '3',
            name: 'Influencias Paises y Usuarios',
            routeName: 'Influencias Paises Usuarios',
          },
        ];
      } else {
        this.navdata = [
          {
            id: '1',
            name: 'Distribución de Tweets',
            routeName: 'Distribucion Tweets',
          },
          {
            id: '2',
            name: 'Distribución de Usuarios',
            routeName: 'Distribucion Usuarios',
          },
          // {
          // id: '3',
          // name: 'Distribución de Deportes',
          // routeName: 'Distribucion Deportes',
          // },
        ];
      }
    },
  },
};
</script>

<style>
#app {
  position: fixed !important;
  width: 100% !important;
  min-height: 100% !important;
}

.container {
  position: fixed !important;
  width: 100% !important;
  min-height: 100% !important;
}

.copyright {
  margin-left: 180px;
}

.el-footer {
  color: #ffffff;
  text-align: center;
  line-height: 60px;
  position: fixed;
  left: 0px;
  bottom: 0px;
  height: 30px;
  width: 100%;
  background: #607d8b;
}

.el-aside {
  color: #333;
  text-align: center;
  line-height: 200px;
}

.el-main {
  background-color: #e9eef3;
  color: #333;
  text-align: center;
  margin-bottom: 60px !important;
  max-height: 900px;
}

body > .el-container {
  margin-bottom: 40px;
}

.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}
</style>
