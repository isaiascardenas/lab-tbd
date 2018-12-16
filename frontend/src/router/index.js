import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/views/Home'
import Main from '@/views/components/Main'
import Fechas from '@/views/popularidad/Fechas'
import Paises from '@/views/popularidad/Paises'
import Deportes from '@/views/popularidad/Deportes'
import DeportesPais from '@/views/popularidad/DeportesPais'

Vue.use(Router)

let routes = [
    {
        path: '/',
        name: 'Home',
        redirect: { name: 'Popularidad Deportes' },
        component: Home,
        children: [
            {
                path: 'popularidad',
                name: 'Main',
                redirect: { name: 'Popularidad Deportes' },
                component: Main,
                children: [
                    {
                        path: 'deportes',
                        name: 'Popularidad Deportes',
                        component: Deportes
                    },
                    {
                        path: 'fechas',
                        name: 'Popularidad Fechas',
                        component: Fechas
                    },
                    {
                        path: 'paisesdeportes',
                        name: 'Popularidad Deportes Paises',
                        component: DeportesPais
                    },
                    {
                        path: 'paises',
                        name: 'Popularidad Paises',
                        component: Paises
                    }
                ]
            }

        ]
    }
]

export default new Router({ routes: routes })
