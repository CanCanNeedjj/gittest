<template>
  <div class="container">
    <div class="above">
      <div class="title">史上最牛GPT</div>
    </div>
    <div class="under">
        <div class="text">
          <el-scrollbar height="85vh">
          <div v-for="(chat,index) in oldTexts" :key="index">
            <div class="question" v-if="chat.type === 'question'" style="display: flex;justify-content: flex-end;margin-bottom: 30px;">
              <div class="info-content">{{chat.content}}</div>
            </div>
            <div class="answer" v-if="chat.type === 'answer'" style="display: flex;justify-content: flex-start;margin-left: 30px">
              <div class="ans-content">{{chat.content}}</div>
            </div>
          </div>
            </el-scrollbar>
        </div>
      <div class="input">
        <el-input v-model="input" placeholder="请输入问题" style="width: 80vh;height: 5vh"/>
        <el-button style="width: 20vh;height: 5vh" type="primary" @click="add" :disabled="abledBut">提交提问<text v-if="abledBut">({{setTimeNum}}S)</text></el-button>
      </div>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";
export default {
  name: 'HomeView',
  components: {},
  data(){
    return{
      input:'',
      chats:[{type:'question',content:'你是谁啊？'},{type:'answer',content: '我是史上最牛GPT'}],
      texts:'',
      oldTexts:[],
      abledBut: false, //是否禁止
      setTimeNum: 2,  // 倒计时时间
      timeWrap: null  // 定时器标识
    }
  },
  watch: {
    /* 使用watch来响应数据的变化 */
    //监听倒计时数据的变化，小于0的时候，清除定时器，解开按钮，重置倒计时
    setTimeNum(newVal, oldVal) {
      if(newVal < 0){
        clearInterval(this.timeWrap)
        this.abledBut = false
        this.setTimeNum = 2
      }
    }
  },
  methods:{
    add(){
      if (this.setTimeNum > 0) {
        this.abledBut = true
        this.timeWrap = setInterval(() => {
          this.setTimeNum -= 1
        }, 1000)
      }
      if (this.input != ''){
        this.$message({
          type:"success",
          message:"请耐心等待！"
        })
        request.get("/api/addChat?question="+this.input).then(res=>{
          if (res.code === "200"){
            this.$message({
              type:"success",
              message:"询问成功"
            })
            this.oldTexts.push({type:'question',content:this.input})
            this.input = ''
            this.texts = res.data
            this.oldTexts.push({type:'answer',content:this.texts})
            this.texts = ''
          }else {
            this.$message({
              type:"error",
              message:"询问失败！"
            })
          }
        })
      }else {
        this.$message({
          type:"error",
          message:"请先填写内容！"
        })
      }

    }
  }
}
</script>
<style>
.container{
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  margin: auto;

  width: 100vh;
  height: 100vh;
}
  .above{
    width: 100vh;
    height: 10vh;
    background-color: deepskyblue;
  }
  .under{
    width: 100vh;
    height: 90vh;
  }
    .title{
      font-size: 7vh;
      color: #fff;
      text-shadow: 0 0 5px #FF0200, 0 0 10px #727272;
      text-align: center;
    }
    .text{
      width: 100vh;
      height: 85vh;
    }
    .input{
      width: 100vh;
      height: 5vh;
      background-color: #727272;
    }
    .info-content{
      word-break: break-all;
      display: inline-block;
      padding: 10px;
      font-size: 14px;
      background: #fff;
      position: relative;
      margin-top: 8px;
      margin-right: 8px;
      background: lightgreen;
      border-radius: 4px;
    }
    .info-content::after {
      position: absolute;
      right: -8px;
      top: 8px;
      content: "";
      border-left: 10px solid lightgreen;
      border-top: 8px solid transparent;
      border-bottom: 8px solid transparent;
    }
    .ans-content{
      word-break: break-all;
      display: inline-block;
      padding: 10px;
      font-size: 14px;
      background: #fff;
      position: relative;
      margin-top: 8px;
      margin-right: 8px;
      background: #dbdbdb;
      border-radius: 4px;
    }
    .ans-content::before {
      position: absolute;
      left: -8px;
      top: 8px;
      content: "";
      border-right: 10px solid #dbdbdb;
      border-top: 8px solid transparent;
      border-bottom: 8px solid transparent;
    }



</style>