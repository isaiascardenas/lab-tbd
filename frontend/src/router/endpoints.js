import axios from 'axios';

let base = 'http://localhost:4040';
let neo = 'http://localhost:4040/neo4j';
// let base = 'http://204.48.18.31:8080/cuatro-0.0.1-SNAPSHOT';
// let neo = 'http://204.48.18.31:8080/cuatro-0.0.1-SNAPSHOT/neo4j';

var DeportesResources = {
  get: params => {
    return axios.get(`${base}/sport/all`, { params: params });
  },
};

var PaisesResources = {
  get: params => {
    return axios.get(`${base}/country/all`, { params: params });
  },
};

var FechasResources = {
  get(params) {
    return axios.get(`${base}/fecha/all`, { params: params });
  },
};

var EstadisticasResources = {
  getPais: params => {
    return axios.get(`${base}/country/${params.pais_id}/statistics`, {
      params: params,
    });
  },
  getDeportes: params => {
    return axios.get(`${base}/sport/${params.sport_id}/statistics`, {
      params: params,
    });
  },
};

var Neo4jResources = {
  getDeportesUsuarios(params) {
    return axios.get(`${neo}/deporte-usuario`, { params: params });
  },
  getPaisesDeportes(params) {
    return axios.get(`${neo}/pais-deporte`, { params: params });
  },
  getPaisesUsuarios(params) {
    return axios.get(`${neo}/pais-usuario`, { params: params });
  },
};

var multipleRequest = {
  all: array => {
    return axios.all(array);
  },
};

export {
  DeportesResources,
  PaisesResources,
  FechasResources,
  EstadisticasResources,
  Neo4jResources,
  multipleRequest,
};
