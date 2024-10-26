<template>
  <div class="chart-container">
    <div id="goodsCountByCategory"></div>
  </div>
</template>

<script setup>
import categoryApi from "@/api/categoryApi.js";
import * as echarts from "echarts";

function init() {
  categoryApi.getGoodsCountByCategory()
      .then(resp => {

        if (Array.isArray(resp)) {
          let data = resp.map(item => ({
            value: item.goods_count,
            name: item.parent_category_name
          }));

          var myChart = echarts.init(document.getElementById('goodsCountByCategory'));

          var option = {
            title: {
              text: '各分类下产品数量',
              top: 20,
              textStyle: {
                color: '#000000'
              }
            },
            tooltip: {
              trigger: 'item',
              formatter: '{a} <br/>{b}: {c} ({d}%)',
              backgroundColor: 'rgba(50, 50, 50, 0.7)',
              borderColor: '#333',
              borderWidth: 1,
              textStyle: {
                color: '#FFFFFF'
              }
            },
            legend: {
              data: data.map(item => item.name),
              right: '10%',
              itemWidth: 10,
              itemHeight: 10,
              textStyle: {
                color: '#000000'
              }
            },
            series: [
              {
                name: '数量',
                type: 'pie',
                radius: ['40%', '70%'],
                data: data.sort((a, b) => a.value - b.value),
                avoidLabelOverlap: false,
                padAngle: 5,
                itemStyle: {
                  borderRadius: 10
                },
                label: {
                  show: false,
                  position: 'center'
                },
                emphasis: {
                  label: {
                    show: true,
                    fontSize: 40,
                    fontWeight: 'bold'
                  }
                },
                labelLine: {
                  show: false
                },
                animationType: 'scale',
                animationEasing: 'elasticOut',
                animationDelay: function (idx) {
                  return Math.random() * 200;
                }
              }
            ]
          };

          myChart.setOption(option);
        } else {
          console.error("API response data is not an array");
        }
      })
      .catch(error => {
        console.error("API request error:", error);
      });
}


init();
</script>

<style scoped>
.chart-container {
  display: flex;
  justify-content: space-between; /* 确保图表在容器中有空间分隔 */
}

#goodsCountByCategory {
  width: 50%;   /* 设置宽度占容器的50% */
  height: 80vh; /* 设置图表的高度 */
}
</style>