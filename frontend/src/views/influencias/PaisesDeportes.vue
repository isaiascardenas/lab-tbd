<template>
  <div class="graph" v-loading="loading">
    <div class="text graph-title">Influencias entre Paises y Deportes</div>
    <d3-network
      :net-nodes="nodes"
      :net-links="links"
      :options="options"
      @node-click="showNode"
    />

    <el-col :span="6" class="details-container">
      <el-card class="box-card" v-show="detailsVisible">
        <div slot="header" class="clearfix">
          <span> {{ details.title }} </span>
          <el-button
            style="float: right; padding: 3px 0"
            @click="closeDetails"
            type="text"
          >
            <i class="el-icon-close"></i>
          </el-button>
        </div>
        <div class="node-detail" v-if="details.show">
          Índice de influencias: <b> {{ details.influencia }} </b>
        </div>
        <div class="node-detail" v-if="details.show">
          Porcentaje de influencias: <b> {{ details.porcentaje }} %</b>
        </div>
        <div class="node-detail" v-else>Deporte</div>
      </el-card>
    </el-col>
  </div>
</template>

<script>
import D3Network from 'vue-d3-network';
import { Neo4jResources } from './../../router/endpoints';

export default {
  components: {
    D3Network,
  },
  data() {
    return {
      nodes: [],
      data: [],
      options: {},
      links: [],
      details: {
        title: '',
        show: true,
        influencia: 0,
        porcentaje: 0,
      },
      detailsVisible: false,
      loading: true,
    };
  },
  mounted() {
    this.getData();
  },
  methods: {
    showNode(event, node) {
      if (node._color == '#d32f2f') {
        this.details.title = node.name;
        this.details.show = false;
        this.detailsVisible = true;
        return;
      }
      this.details.show = true;
      this.details.title = node.name;
      this.details.porcentaje = this.setPercent(node.id);
      this.details.influencia = this.data[node.id].influencia;
      this.detailsVisible = true;
    },
    closeDetails() {
      this.detailsVisible = false;
    },
    getData() {
      let self = this;
      Neo4jResources.getPaisesDeportes({})
        .then(response => {
          this.data = response.data.nodes;
          this.fillData(response.data.nodes, response.data.links);
        })
        .catch(error => {
          console.log(error);
        })
        .finally(() => {
          this.loading = false;
        });
    },
    fillData(nodes, links) {
      let i = 0;

      _.each(nodes, node => {
        if (node.label == 'Deporte') {
          this.nodes.push({
            id: i,
            name: node.name,
            _color: '#d32f2f',
            _size: 50,
          });
        } else {
          this.nodes.push({
            id: i,
            name: node.name,
            _color: '#00aaff',
            _size: this.setNodeSize(node.influencia),
          });
        }
        i++;
      });

      _.each(links, link => {
        this.links.push({
          sid: link.sid,
          tid: link.tid,
          _color: '#333333',
        });
      });

      this.options = {
        force: 3000,
        size: { w: 1200, h: 480 },
        nodeSize: 10,
        nodeLabels: true,
        canvas: false,
        linkWidth: 1,
        strLinks: true,
      };
    },
    setNodeSize(influencia) {
      if (influencia > 3000) {
        return 80;
      } else if (influencia < 100) {
        return 30;
      }

      return Math.floor((50 / 2900) * influencia + 30);
    },
    setPercent(index) {
      let total = _.reduce(
        this.data,
        (sum, n) => {
          if (n.influencia) {
            return sum + n.influencia;
          }
          return sum + 0;
        },
        0
      );

      return (
        Math.floor(((this.data[index].influencia * 100) / total) * 10) / 10
      );
    },
  },
};
</script>

<style>
.graph-title {
  margin-bottom: 20px;
}

.node-label {
  font-size: 15px;
}

.net {
  z-index: 0;
  position: relative;
}

.details-container {
  margin-top: -180px;
  z-index: 1;
  position: relative;
}

.graph {
}
</style>
