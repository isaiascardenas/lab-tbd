<template>
    <el-row class="main-container">
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
    overflow: scroll;
    margin-bottom: 240px;
}

</style>
