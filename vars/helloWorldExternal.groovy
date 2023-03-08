def call(Map config = [:]){
    loadLinuxScript(name: 'hello-world.sh')
    sh "./hello-worls.sh ${config.name} ${config.dayOfWeek}"
    }