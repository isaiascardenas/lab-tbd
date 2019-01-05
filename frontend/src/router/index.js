import Vue from 'vue';
import Router from 'vue-router';
import Home from '@/views/Home';
import Main from '@/views/components/Main';
import Fechas from '@/views/popularidad/Fechas';
import Paises from '@/views/popularidad/Paises';
import Deportes from '@/views/popularidad/Deportes';
import DeportesUsuarios from '@/views/influencias/DeportesUsuarios';
import EstadisticasPais from '@/views/popularidad/EstadisticasPais';

Vue.use(Router);

let routes = [
  {
    path: '/',
    name: 'Home',
    redirect: { name: 'Popularidad Deportes' },
    component: Home,
    children: [
      {
        path: '',
        name: 'Main',
        redirect: { name: 'Popularidad Deportes' },
        component: Main,
        children: [
          {
            path: 'popularidad',
            redirect: { name: 'Popularidad Deportes' },
            component: { template: '<router-view></router-view>' },
            children: [
              {
                path: 'deportes',
                name: 'Popularidad Deportes',
                component: Deportes,
              },
              {
                path: 'paises',
                name: 'Popularidad Paises',
                component: Paises,
              },
              {
                path: 'fechas',
                name: 'Popularidad Fechas',
                component: Fechas,
              },
              {
                path: 'paisesdeportes/:id',
                name: 'Estadisticas Pais',
                component: EstadisticasPais,
              },
            ],
          },
          {
            path: 'influencias',
            redirect: { name: 'Influencias Usuarios' },
            component: { template: '<router-view></router-view>' },
            children: [
              {
                path: 'usuarios',
                name: 'Influencias Usuarios',
                component: DeportesUsuarios,
              },
            ],
          },
        ],
      },
    ],
  },
];

export default new Router({ routes: routes });
