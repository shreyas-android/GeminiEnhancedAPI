package com.androidai.module.geminiapi.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.text.HtmlCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.androidai.framework.shared.core.model.GeminiAIAndroidCore
import com.androidai.module.geminiapi.ui.viewmodel.GeminiAIDemoViewModel
import com.androidai.module.geminiapi.ui.viewmodel.GeminiAIDemoViewModelFactory
import com.androidai.module.geminiapi.R
import com.cogniheroid.framework.feature.appaai.ui.generation.advancetextgeneration.uistate.GeminiAIDemoUIEffect
import com.androidai.module.geminiapi.uistate.GeminiAIDemoUIEvent
import com.androidai.module.geminiapi.uistate.GeminiAIDemoUIState
import com.androidai.module.geminiapi.utils.ContentUtils
import com.androidai.module.geminiapi.utils.AttachmentFileUtils
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeminiAIDemoScreen(navigateBack: () -> Unit) {

    val geminiAIDemoViewModelFactory = remember{
        GeminiAIDemoViewModelFactory(GeminiAIAndroidCore.getGeminiAIManagerInstance())
    }

    val geminiAIDemoViewModel = viewModel<GeminiAIDemoViewModel>(
        factory = geminiAIDemoViewModelFactory
    )

    val context = LocalContext.current
    val pickerLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
       geminiAIDemoViewModel.performIntent(
           GeminiAIDemoUIEvent.UpdateFileUriItems(
           AttachmentFileUtils.getFileUriInfoListFromActivityResult(context = context, it)))
    }

    LaunchedEffect(key1 = Unit) {
        geminiAIDemoViewModel.fatherAIInfoUIEffectFlow.collectLatest {
            when (it) {
                GeminiAIDemoUIEffect.LaunchPicker -> {
                    pickerLauncher.launch(AttachmentFileUtils.getAttachmentIntent())
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {

        val colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
        )
        Surface(tonalElevation = 3.dp) {
            TopAppBar(modifier = Modifier, colors = colors, title = {
                Text(
                    text = stringResource(id = R.string.app_name),
                    fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface
                )
            })
        }
        FatherAIInfoView(modifier = Modifier.fillMaxSize(),
            geminiAIDemoUIState = geminiAIDemoViewModel.geminiAIDemoUIStateStateFlow.collectAsState().value,
            performIntent = { textGenerationUIEvent ->
                geminiAIDemoViewModel.performIntent(textGenerationUIEvent)
            })
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FatherAIInfoView(
        modifier: Modifier,
        geminiAIDemoUIState : GeminiAIDemoUIState,
        performIntent: (GeminiAIDemoUIEvent) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    AdUIContainer(modifier = modifier.navigationBarsPadding(), content = { childModifier->
        Column(
            modifier = childModifier
                .imePadding()
                .verticalScroll(rememberScrollState())
                .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
        ) {

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
            ) {

                val (image, textInput) = createRefs()
                Box(modifier = Modifier
                    .constrainAs(image) {
                        top.linkTo(textInput.top)
                        bottom.linkTo(textInput.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(textInput.start)
                    }
                    .padding(start = 16.dp, end = 8.dp)
                    .background(
                        shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer)
                    .padding(8.dp)) {
                    Icon(
                        modifier = Modifier
                            .clickable {
                                focusManager.clearFocus()
                                performIntent(GeminiAIDemoUIEvent.OnOpenFilePicker)
                            },
                        painter = painterResource(id = R.drawable.ic_add_photo),
                        contentDescription = "", tint = MaterialTheme.colorScheme.primary
                    )
                }

                TextInputField(modifier = Modifier.constrainAs(textInput) {
                    top.linkTo(parent.top)
                    start.linkTo(image.end)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
                    geminiAIDemoUIState.inputText,
                    onInputTextChange = { inputData ->
                        performIntent(GeminiAIDemoUIEvent.InputText(inputData))
                    },
                    onClear = {
                        performIntent(GeminiAIDemoUIEvent.ClearText)
                    })

            }

            if (geminiAIDemoUIState.fileUriInfoItems.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .wrapContentSize()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                }
            }

            val defaultMessage = stringResource(id = R.string.message_default_error_ai)

            CustomButton(
                modifier = Modifier.padding(top = 16.dp),
                label = stringResource(R.string.label_generate_text),
                onClick = {
                    focusManager.clearFocus()
                    performIntent(
                        GeminiAIDemoUIEvent.GenerateText(
                            geminiAIDemoUIState.inputText, defaultMessage))
                })

            CustomButton(
                modifier = Modifier.padding(top = 16.dp),
                label = stringResource(R.string.label_all_files),
                onClick = {
                    focusManager.clearFocus()
                    performIntent(
                        GeminiAIDemoUIEvent.GetAllFiles)
                })

            CustomButton(
                modifier = Modifier.padding(top = 16.dp),
                label = stringResource(R.string.label_get_file_by_name),
                onClick = {
                    focusManager.clearFocus()
                    performIntent(
                        GeminiAIDemoUIEvent.GetFile)
                })

            CustomButton(
                modifier = Modifier.padding(top = 16.dp),
                label = stringResource(R.string.label_delete_file_by_name),
                onClick = {
                    focusManager.clearFocus()
                    performIntent(
                        GeminiAIDemoUIEvent.DeleteFirstFile)
                })


            if (geminiAIDemoUIState.outputText.isNotEmpty() || geminiAIDemoUIState.isGenerating) {

                if (!geminiAIDemoUIState.isGenerating) {
                    Text(
                        text = stringResource(id = R.string.label_generated_by_gemini),
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(top = 32.dp, bottom = 16.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                val cardColors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )

                Card(
                    modifier = Modifier.padding(16.dp),
                    colors = cardColors,
                    elevation = CardDefaults.outlinedCardElevation(defaultElevation = 3.dp)
                ) {
                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        val (text, share, copy) = createRefs()

                        if (!geminiAIDemoUIState.isGenerating) {
                            Icon(modifier = Modifier
                                .size(28.dp)
                                .padding(top = 8.dp, end = 8.dp)
                                .constrainAs(share) {
                                    top.linkTo(parent.top)
                                    end.linkTo(copy.start)

                                }
                                .clickable {
                                    focusManager.clearFocus()
                                    ContentUtils.shareContent(
                                        context = context, data = geminiAIDemoUIState.outputText)
                                },
                                painter = painterResource(id = R.drawable.ic_share),
                                contentDescription = "", tint = MaterialTheme.colorScheme.primary
                            )

                            Icon(modifier = Modifier
                                .size(28.dp)
                                .padding(top = 8.dp, end = 8.dp)
                                .constrainAs(copy) {
                                    top.linkTo(parent.top)
                                    end.linkTo(parent.end)
                                }
                                .clickable {
                                    focusManager.clearFocus()
                                    ContentUtils.copyAndShowToast(
                                        context = context, result = geminiAIDemoUIState.outputText)
                                },
                                painter = painterResource(id = R.drawable.ic_copy),
                                contentDescription = "", tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        val result = if (geminiAIDemoUIState.isGenerating) {
                            stringResource(id = R.string.placeholder_advance_generate_text)
                        } else {
                            geminiAIDemoUIState.outputText
                        }

                        Text(text = HtmlCompat.fromHtml(result, 0).toString(), fontSize = 16.sp,
                            modifier = Modifier
                                .constrainAs(text) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                    end.linkTo(share.start)
                                    bottom.linkTo(parent.bottom)
                                    width = Dimension.fillToConstraints
                                }
                                .combinedClickable(onClick = {
                                    ContentUtils.copyAndShowToast(
                                        context = context, result = geminiAIDemoUIState.outputText)
                                }, onLongClick = {
                                    ContentUtils.copyAndShowToast(
                                        context = context, result = geminiAIDemoUIState.outputText)
                                })
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    })

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TextInputField(
    modifier: Modifier = Modifier,
    inputText: String,
    onInputTextChange: (String) -> Unit,
    onClear: () -> Unit
) {

    OutlinedTextField(
        value = inputText,
        onValueChange = {
            onInputTextChange(it)
        },
        label = {
            Text(text = stringResource(R.string.hint_generate_text))
        },
        modifier = modifier
            .padding(start = 8.dp, end = 16.dp)
            .fillMaxWidth(), trailingIcon = {
            if (inputText.isNotEmpty()) {
                IconButton(onClick = {
                    onClear()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = "", tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    )
}

@Composable
fun AdUIContainer(modifier : Modifier, content: @Composable (Modifier) -> Unit){
    ConstraintLayout(modifier = modifier) {
        val (container) = createRefs()

        content(Modifier
            .constrainAs(container) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                height = Dimension.fillToConstraints
                width = Dimension.fillToConstraints
            })

    }
}

@Composable
fun CustomButton(modifier: Modifier = Modifier, label: String, onClick: () -> Unit) {
    val buttonColors = ButtonDefaults.textButtonColors(
        containerColor = MaterialTheme.colorScheme.onPrimaryContainer
    )
    TextButton(
        shape = RoundedCornerShape(8.dp),
        colors = buttonColors, onClick = {
            onClick()
        },
        modifier = modifier.padding(vertical = 16.dp, horizontal = 32.dp)
    ) {
        androidx.compose.material.Text(
            color = MaterialTheme.colorScheme.onSecondary,
            text = label,
            fontSize = 16.sp
        )
    }
}


