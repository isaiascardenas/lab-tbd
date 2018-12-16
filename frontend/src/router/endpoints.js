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
        return axios.get(`${base}/dates/all`, {params: params});
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

export { DeportesResources, PaisesResources, FechasResources};

