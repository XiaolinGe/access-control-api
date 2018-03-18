package com.github.leon.generator.entity


import com.github.leon.generator.task.processor.ITaskProcessor
import org.apache.commons.lang3.StringUtils
import org.joor.Reflect


data class Task(
        var id: Int? = null,

        var name: String,

        var taskType: String,

        var taskOfProject:TaskOfProject,

        var folder: String,

        var filename: String,

        var templatePath: String
) {

    fun run(codeProject: CodeProject, root: MutableMap<String, Any>): List<String> {
        val paths = taskProcessor(this.taskType).run(codeProject, this, root)
        return paths
    }

    private fun taskProcessor(taskType: String): ITaskProcessor {
        return Reflect.on(ITaskProcessor::class.java.`package`.name + "."
                + StringUtils.capitalize(taskType) +
                "TaskProcessor").create().get()
    }


}
