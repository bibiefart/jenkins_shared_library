def call(Map config = [:]){
    loadLinuxScript(name: 'hello-worls.sh')
    sh "./hello-worls.sh ${config.name} ${config.dayOfWeek}"
    }