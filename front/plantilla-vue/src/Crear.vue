<template>
    <div class="center">
        <h2>Crear Actor</h2>
        <form>
            Nombre:<br>
            <input v-model="Actor.firstName"><br>
            Apellido:<br>
            <input v-model="Actor.lastName">
        </form>

        <button @click="crearActor">Crear</button>
    </div>
</template>

<script>
    export default {
        data(){
            return{
                Actor: {firstName: "", lastName: ""}
            }
        },
        methods: {
            crearActor()
            {
                console.log(this.Actor);
                this.$http.post('http://localhost:8081/sakila-spring-backend/actors/', this.Actor)
                    .then(response=>{
                        console.log(response);
                    }).catch(response=>{
                        console.log('error cargando lista');
                    });
            }
        }
    }
</script>

<style scoped>

</style>