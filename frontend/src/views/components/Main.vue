<template>
  <el-row class="main-container">
    <el-row class="main-menu-container">
      <el-menu
        @select="handleMenu"
        default-active="1"
        class="main-menu"
        mode="horizontal"
      >
        <el-menu-item v-for="data in navdata" :key="data.id" :index="data.id">{{
          data.name
        }}</el-menu-item>

        <el-menu-item index="4" v-if="$route.params.sprint == 'popularidad'">
          <el-dropdown @command="handleCommand">
            <span class="el-dropdown-link">
              Deportes por País<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item
                v-for="pais in paises"
                :key="pais.countryId"
                :command="pais.countryId"
                >{{ pais.countryName }}</el-dropdown-item
              >
            </el-dropdown-menu>
          </el-dropdown>
        </el-menu-item>

        <el-menu-item
          index="4"
          v-else-if="$route.params.sprint == 'distribucion'"
        >
          <el-dropdown @command="handleCommand">
            <span class="el-dropdown-link">
              Distribución de Deportes
              <i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item
                v-for="pais in paises"
                :key="pais.countryId"
                :command="pais.countryId"
                >{{ pais.countryName }}</el-dropdown-item
              >
            </el-dropdown-menu>
          </el-dropdown>
        </el-menu-item>
      </el-menu>
    </el-row>

    <el-row class="main-body-container">
      <el-card class="main-card">
        <router-view :key="$route.params.id"></router-view>
      </el-card>
    </el-row>
  </el-row>
</template>

<script>
import { PaisesResources } from './../../router/endpoints';

export default {
  name: 'Main',
  props: ['navdata'],
  data() {
    return {
      paises: [],
    };
  },
  mounted() {
    this.getDeportes();
  },
  methods: {
    handleMenu(index) {
      if (index != 4) {
        let routeName = this.navdata.find(x => x.id == index).routeName;
        this.$router.push({
          name: routeName,
          params: { sprint: this.$route.params.sprint },
        });
      }
    },
    handleCommand(countryId) {
      if (this.$route.params.sprint == 'popularidad') {
        this.$router.push({
          name: 'Estadisticas Pais',
          params: { id: countryId, sprint: this.$route.params.sprint },
        });
        return;
      }
      this.$router.push({
        name: 'Distribucion Deportes',
        params: { id: countryId, sprint: this.$route.params.sprint },
      });
    },
    getDeportes() {
      PaisesResources.get({}).then(response => {
        this.paises = response.data;
      });
    },
  },
};
</script>

<style scoped>
.main-body-container {
  height: 100%;
}

.main-menu-container {
  margin-bottom: 10px;
}

.main-container {
  height: 100%;
}

.main-card {
  margin-bottom: 240px;
}
</style>
