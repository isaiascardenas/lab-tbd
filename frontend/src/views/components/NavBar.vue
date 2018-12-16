<template>
    <el-header>
        <el-menu
            @select="handleMenu"
            default-active="1"
            background-color="#512DA8"
            text-color="#ffffff"
            active-text-color="#00bcd4"
            class="navbar-menu"
            mode="horizontal">
            <el-menu-item index="1">Option 1</el-menu-item>
            <el-menu-item index="2">Option 2</el-menu-item>
            <el-menu-item index="3">Option 3</el-menu-item>
        </el-menu>

    </el-header>
</template>

<script>

export default {
    name: 'NavBar',
    data () {
        return {
        }
    },
    methods: {
        handleMenu (index) {
            console.log('holi', index);
            if(index == 1){
                console.log('Entre al 1')
                this.$router.push({name: 'Popularidad Deportes'})
            }
            else if(index == 2){
                console.log('Entre al if')
                this.$router.push({name: 'Popularidad Paises'})
            }
        }
    }

}
</script>

<style scoped>

.el-header {
    background-color: #512DA8;
    color: #ffffff;
    text-align: center;
    line-height: 65px;
}

</style>
