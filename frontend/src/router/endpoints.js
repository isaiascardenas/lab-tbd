import axios from 'axios';

export const paisesResource = {
    get (params) {
        return axios.get(`http://localhost:4040/country/all`, { params: params });
    },
    show (params) {
        return axios.get(`/proyectos/${params.id}`, {params: params});
    },
    add (params) {
        return axios.post(`/proyectos`, params);
    },
    remove (params) {
        return axios.delete(`/proyectos/${params.id}`, {params: params});
    },
    edit (params) {
        return axios.put(`/proyectos/${params.id}`, params);
    }
};
