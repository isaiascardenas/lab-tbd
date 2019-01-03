import axios from 'axios';

let base = 'http://204.48.18.31:8080/cuatro-0.0.1-SNAPSHOT';

var DeportesResources = {
    get: (params) => {
        return axios.get(`${base}/sport/all`, {params: params});
    },
    add(params) {
        return ;
    },
    remove(params) {
        return ;
    },
    edit(params) {
        return ;
    }
};

var PaisesResources = {
    get: (params) => {
        return axios.get(`${base}/country/all`, {params: params});
    },
    add(params) {
        return ;
    },
    remove(params) {
        return ;
    },
    edit(params) {
        return ;
    }
};

var FechasResources = {
    get(params) {
        return axios.get(`${base}/fecha/all`, {params: params});
    },
    add(params) {
        return ;
    },
    remove(params) {
        return ;
    },
    edit(params) {
        return ;
    }
};

var DeportesPaisResources = {
    getPaisesEstadisticas: (params) => {
        return axios.get(`${base}/country/${params.pais_id}/statistics`, {params: params});
    },
    getEstadisticasDeportes: (params) => {
        return axios.get(`${base}/statistic/${params.statistic_id}/sport`, {params: params});
    },
    remove(params) {
        return ;
    },
    edit(params) {
        return ;
    }
};

var multipleRequest = {
    all: (array) => {
        return axios.all(array);
    }
};

export { DeportesResources, PaisesResources, FechasResources, DeportesPaisResources, multipleRequest };

