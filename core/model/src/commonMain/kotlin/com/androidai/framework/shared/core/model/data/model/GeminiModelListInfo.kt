package com.androidai.framework.shared.core.model.data.model

data class GeminiModelListInfo(
        val fileInputList : List<ModelInput.File>,
        val textInputList : List<ModelInput.Text>, val blobInputList : List<ModelInput.Blob>,
        val imageInputList : List<ModelInput.Image>)

fun List<ModelInput>.toGeminiModelListInfo() : GeminiModelListInfo {
    val fileInputList = arrayListOf<ModelInput.File>()
    val textInputList = arrayListOf<ModelInput.Text>()
    val blobInputList = arrayListOf<ModelInput.Blob>()
    val imageInputList = arrayListOf<ModelInput.Image>()

    this.forEach {
        when(it) {
            is ModelInput.Blob -> {
                blobInputList.add(it)
            }

            is ModelInput.File -> {
                fileInputList.add(it)
            }

            is ModelInput.Text -> {
                textInputList.add(it)
            }

            is ModelInput.Image -> {
                imageInputList.add(it)
            }
        }
    }

    return GeminiModelListInfo(fileInputList, textInputList, blobInputList, imageInputList)
}