<template>

    <el-aside width="200px">
            <el-col class="sidebar">
                <el-header class="sidebar-header">
                    <span>MI WEBSITE</span>
                </el-header >
                <el-row class="menu-sidebar">
                    <el-menu
                        default-active="1"
                        class="el-menu-vertical"
                        background-color="#474885"
                        text-color="#fff"
                        active-text-color="#00BCD4">

                        <el-menu-item index="1">
                            <i class="el-icon-menu"></i>
                            <span>Sprint 1</span>
                        </el-menu-item>
                        <el-menu-item index="2">
                            <i class="el-icon-document"></i>
                            <span>Sprint 2</span>
                        </el-menu-item>
                        <el-menu-item index="3">
                            <i class="el-icon-setting"></i>
                            <span>Sprint 3</span>
                        </el-menu-item>

                    </el-menu>
                </el-row>
            </el-col>
    </el-aside>

</template>

<script>

export default {
    name: 'SideBar',
    data () {
        return {
        }
    }
}
</script>

<style scoped>

.el-menu-vertical, .menu-sidebar, .sidebar {
    height: 100%;
}

.sidebar-header{
    background-color: #673AB7;
    color: #ffffff;
    text-align: center;
    line-height: 65px;
}

</style>
