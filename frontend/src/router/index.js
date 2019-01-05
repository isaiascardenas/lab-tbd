import Vue from 'vue';
import Router from 'vue-router';
import Home from '@/views/Home';
import Main from '@/views/components/Main';
import Fechas from '@/views/popularidad/Fechas';
import Paises from '@/views/popularidad/Paises';
import Deportes from '@/views/popularidad/Deportes';
import PaisesDeportes from '@/views/influencias/PaisesDeportes';
import PaisesUsuarios from '@/views/influencias/PaisesUsuarios';
import DeportesUsuarios from '@/views/influencias/DeportesUsuarios';
import EstadisticasPais from '@/views/popularidad/EstadisticasPais';

Vue.use(Router);

let routes = [
  {
    path: '/',
    name: 'Home',
    redirect: {
      name: 'Popularidad Deportes',
      params: { sprint: 'popularidad' },
    },
    component: Home,
    children: [
      {
        path: '',
        name: 'Main',
        redirect: {
          name: 'Popularidad Deportes',
          params: { sprint: 'popularidad' },
        },
        component: Main,
        children: [
          {
            path: ':sprint',
            redirect: {
              name: 'Popularidad Deportes',
              params: { sprint: 'popularidad' },
            },
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
              {
                path: 'deporte-usuario',
                name: 'Influencias Deportes Usuarios',
                component: DeportesUsuarios,
              },
              {
                path: 'pais-deporte',
                name: 'Influencias Paises Deportes',
                component: PaisesDeportes,
              },
              {
                path: 'pais-usuario',
                name: 'Influencias Paises Usuarios',
                component: PaisesUsuarios,
              },
            ],
          },
        ],
      },
    ],
  },
];

export default new Router({ routes: routes });
