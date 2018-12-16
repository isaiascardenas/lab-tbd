<template>
    <el-row class="main-container">

        <el-row class="main-menu-container">
            <el-menu @select="handleMenu" default-active="1" class="main-menu" mode="horizontal">
                <el-menu-item v-for="data in navdata" :key="data.id" :index="data.id">{{ data.name }}</el-menu-item>
            </el-menu>
        </el-row>

        <el-row class="main-body-container">
            <el-card class="main-card">
                <router-view></router-view>
            </el-card>
        </el-row>

    </el-row>
</template>

<script>

export default {
    name: 'Main',
    props: ['navdata'],
    data () {
        return {
        }
    },
    methods: {
        handleMenu (index) {
            let routeName = this.navdata.find(x => x.id == index).routeName;
            this.$router.push({name: routeName})
        }
    }
}
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
