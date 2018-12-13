import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/views/Home'
import Main from '@/views/components/Main'
import Deportes from '@/views/popularidad/Deportes'

Vue.use(Router)

let routes = [
    {
        path: '/',
        name: 'Home',
        component: Home,
        children: [
            {
                path: '/popularidad',
                name: 'Main',
                redirect: { name: 'Popularidad Deportes' },
                component: Main,
                children: [
                    {
                        path: '/deportes',
                        name: 'Popularidad Deportes',
                        component: Deportes
                        // children: []
                    }
                ]
            }

        ]
    }
]

export default new Router({ routes: routes })
