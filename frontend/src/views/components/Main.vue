<template>
    <el-row class="main-container">

        <el-row class="main-menu-container">
            <el-menu :default-active="activeIndex" class="main-menu main-card" mode="horizontal" @select="handleSelect">
                <el-menu-item index="1">Option 1</el-menu-item>
                <el-menu-item index="2">Option 2</el-menu-item>
                <el-menu-item index="3">Option 3</el-menu-item>
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
    data () {
        return {
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

</style>
