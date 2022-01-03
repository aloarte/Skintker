package com.p4r4d0x.skintker.presenter.home.view.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.domain.log.Answer
import com.p4r4d0x.skintker.domain.log.PossibleAnswer
import com.p4r4d0x.skintker.domain.log.Question
import com.p4r4d0x.skintker.theme.SkintkerTheme

@Composable
fun SingleChoiceQuestion(
    possibleAnswer: PossibleAnswer.SingleChoice,
    answer: Answer.SingleChoice?,
    onAnswerSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = possibleAnswer.optionsStringRes.associateBy { stringResource(id = it) }
    val radioOptions = options.keys.toList()
    val selected = if (answer != null) {
        stringResource(id = answer.answer)
    } else {
        null
    }
    val (selectedOption, onOptionSelected) = remember(answer) { mutableStateOf(selected) }
    Column(modifier = modifier) {
        radioOptions.forEach { text ->
            val onClickHandle = {
                onOptionSelected(text)
                options[text]?.let { onAnswerSelected(it) }
                Unit
            }
            val optionSelected = text == selectedOption

            val answerBorderColor = if (optionSelected) {
                MaterialTheme.colors.primary.copy(alpha = 0.5f)
            } else {
                MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
            }

            val answerBackgroundColor = if (optionSelected) {
                MaterialTheme.colors.primary.copy(alpha = 0.12f)
            } else {
                MaterialTheme.colors.background
            }
            Surface(
                shape = MaterialTheme.shapes.small,
                border = BorderStroke(
                    width = 1.dp,
                    color = answerBorderColor
                ),
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = optionSelected,
                            onClick = onClickHandle
                        )
                        .background(answerBackgroundColor)
                        .padding(vertical = 6.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = text
                    )

                    RadioButton(
                        selected = optionSelected,
                        onClick = onClickHandle,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colors.primary
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun MultipleChoiceQuestion(
    possibleAnswer: PossibleAnswer.MultipleChoice,
    answer: Answer.MultipleChoice?,
    onAnswerSelected: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = possibleAnswer.optionsStringRes.associateBy { stringResource(id = it) }
    Column(modifier = modifier) {
        for (option in options) {
            var checkedState by remember(answer) {
                val selectedOption = answer?.answersStringRes?.contains(option.value)
                mutableStateOf(selectedOption ?: false)
            }
            val answerBorderColor = if (checkedState) {
                MaterialTheme.colors.primary.copy(alpha = 0.5f)
            } else {
                MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
            }
            val answerBackgroundColor = if (checkedState) {
                MaterialTheme.colors.primary.copy(alpha = 0.12f)
            } else {
                MaterialTheme.colors.background
            }
            Surface(
                shape = MaterialTheme.shapes.small,
                border = BorderStroke(
                    width = 1.dp,
                    color = answerBorderColor
                ),
                modifier = Modifier
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(answerBackgroundColor)
                        .clickable(
                            onClick = {
                                checkedState = !checkedState
                                onAnswerSelected(option.value, checkedState)
                            }
                        )
                        .padding(vertical = 6.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = option.key)
                    Checkbox(
                        checked = checkedState,
                        onCheckedChange = { selected ->
                            checkedState = selected
                            onAnswerSelected(option.value, selected)
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colors.primary
                        ),
                    )
                }
            }
        }
    }
}

@Composable
fun SliderQuestion(
    possibleAnswer: PossibleAnswer.Slider,
    answer: Answer.Slider?,
    onAnswerSelected: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var sliderPosition by remember {
        mutableStateOf(answer?.answerValue ?: possibleAnswer.defaultValue)
    }
    Row(modifier = modifier) {

        Slider(
            value = sliderPosition,
            onValueChange = {
                sliderPosition = it
                onAnswerSelected(it)
            },
            valueRange = possibleAnswer.range,
            steps = possibleAnswer.steps,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        )
    }
    Row {
        Text(
            text = stringResource(id = possibleAnswer.startText),
            style = MaterialTheme.typography.caption,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.8f)
        )
        Text(
            text = stringResource(id = possibleAnswer.neutralText),
            style = MaterialTheme.typography.caption,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.8f)
        )
        Text(
            text = stringResource(id = possibleAnswer.endText),
            style = MaterialTheme.typography.caption,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.8f)
        )
    }
}

@Composable
fun DoubleSliderQuestion(
    possibleAnswer: PossibleAnswer.DoubleSlider,
    answer: Answer.DoubleSlider?,
    onAnswerSelected: (Float, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var firstSliderPosition by remember {
        mutableStateOf(answer?.answerValueFirstSlider ?: possibleAnswer.defaultValueFirstSlider)
    }
    var secondSliderPosition by remember {
        mutableStateOf(answer?.answerValueSecondSlider ?: possibleAnswer.defaultValueSecondSlider)
    }
    Row(modifier = modifier) {
        Slider(
            value = firstSliderPosition,
            onValueChange = {
                firstSliderPosition = it
                onAnswerSelected(firstSliderPosition, secondSliderPosition)
            },
            valueRange = possibleAnswer.range,
            steps = possibleAnswer.steps,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        )
    }
    Row {
        Text(
            text = stringResource(id = possibleAnswer.startTextFirstSlider),
            style = MaterialTheme.typography.caption,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.8f)
        )
        Text(
            text = stringResource(id = possibleAnswer.endTextFirstSlider),
            style = MaterialTheme.typography.caption,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.8f)
        )
    }
    Row(modifier = modifier) {
        Slider(
            value = secondSliderPosition,
            onValueChange = {
                secondSliderPosition = it
                onAnswerSelected(firstSliderPosition, secondSliderPosition)
            },
            valueRange = possibleAnswer.range,
            steps = possibleAnswer.steps,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        )
    }
    Row {
        Text(
            text = stringResource(id = possibleAnswer.startTextSecondSlider),
            style = MaterialTheme.typography.caption,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.8f)
        )
        Text(
            text = stringResource(id = possibleAnswer.endTextSecondSlider),
            style = MaterialTheme.typography.caption,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.8f)
        )
    }
}

@Composable
fun SingleTextInput(
    possibleAnswer: PossibleAnswer.SingleTextInput,
    answer: Answer.SingleTextInput?,
    onAnswerSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val hint = stringResource(id = possibleAnswer.hint)
    var inputText by rememberSaveable { mutableStateOf(hint) }

    Column(modifier = modifier) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = inputText,
            textStyle = MaterialTheme.typography.caption,
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colors.primary,
                backgroundColor = MaterialTheme.colors.background,
                cursorColor = MaterialTheme.colors.primary,
                disabledLabelColor = MaterialTheme.colors.background,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            onValueChange = {
                if (it.length <= possibleAnswer.maxCharacters) {
                    inputText = it
                    onAnswerSelected(inputText)
                }
            },
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            trailingIcon = {
                if (inputText.isNotEmpty()) {
                    IconButton(onClick = { inputText = "" }) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = null
                        )
                    }
                }
            }
        )
        Text(
            text = "${inputText.length} / ${possibleAnswer.maxCharacters}",
            style = MaterialTheme.typography.caption,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            textAlign = TextAlign.End,
            color = MaterialTheme.colors.primary
        )
    }
}

@Composable
fun SingleTextInputSingleChoice(
    possibleAnswer: PossibleAnswer.SingleTextInputSingleChoice,
    answer: Answer.SingleTextInputSingleChoice?,
    onAnswerSelected: (String, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val textAnswerSaved = stringResource(id = possibleAnswer.hint)
    val optionSelectedAnswerSaved = -1
    var inputText by rememberSaveable { mutableStateOf(textAnswerSaved) }
    var inputOption by rememberSaveable { mutableStateOf(optionSelectedAnswerSaved) }
    val options = possibleAnswer.optionsStringRes.associateBy { stringResource(id = it) }
    val radioOptions = options.keys.toList()
    val selected = if (answer != null) {
        stringResource(id = answer.answer)
    } else {
        null
    }
    val (selectedOption, onOptionSelected) = remember(answer) { mutableStateOf(selected) }

    Column(modifier = modifier) {
        Column {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp, horizontal = 11.dp),
                value = inputText,
                textStyle = MaterialTheme.typography.caption,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.colors.primary,
                    backgroundColor = MaterialTheme.colors.background,
                    cursorColor = MaterialTheme.colors.primary,
                    disabledLabelColor = MaterialTheme.colors.background,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                onValueChange = {
                    if (it.length <= possibleAnswer.maxCharacters) {
                        inputText = it
                        onAnswerSelected(inputText, inputOption)
                    }
                },
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                trailingIcon = {
                    if (inputText.isNotEmpty()) {
                        IconButton(onClick = { inputText = "" }) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = null
                            )
                        }
                    }
                }
            )
            Text(
                text = "${inputText.length} / ${possibleAnswer.maxCharacters}",
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                textAlign = TextAlign.End,
                color = MaterialTheme.colors.primary
            )
        }
        Column(modifier = modifier) {
            radioOptions.forEach { text ->
                val onClickHandle = {
                    onOptionSelected(text)
                    options[text]?.let {
                        inputOption = it
                        onAnswerSelected(inputText, it)
                    }
                    Unit
                }
                val optionSelected = text == selectedOption

                val answerBorderColor = if (optionSelected) {
                    MaterialTheme.colors.primary.copy(alpha = 0.5f)
                } else {
                    MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
                }

                val answerBackgroundColor = if (optionSelected) {
                    MaterialTheme.colors.primary.copy(alpha = 0.12f)
                } else {
                    MaterialTheme.colors.background
                }
                Surface(
                    shape = MaterialTheme.shapes.small,
                    border = BorderStroke(
                        width = 1.dp,
                        color = answerBorderColor
                    ),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = optionSelected,
                                onClick = onClickHandle
                            )
                            .background(answerBackgroundColor)
                            .padding(vertical = 6.dp, horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = text
                        )

                        RadioButton(
                            selected = optionSelected,
                            onClick = onClickHandle,
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colors.primary
                            )
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun MultipleAnswerQuestionPreview() {
    val question = Question(
        id = 1,
        questionText = R.string.multiple_answer_title,
        answer = PossibleAnswer.MultipleChoice(
            optionsStringRes = listOf(
                R.string.multiple_answer_1,
                R.string.multiple_answer_2,
                R.string.multiple_answer_3
            )
        ),
        description = R.string.multiple_description
    )
    SkintkerTheme {
        QuestionContent(
            question = question,
            answer = null,
            onAnswer = {},
        )
    }
}

@Preview
@Composable
fun SingleAnswerQuestionPreview() {
    val question = Question(
        id = 2,
        questionText = R.string.single_answer_title,
        answer = PossibleAnswer.SingleChoice(
            optionsStringRes = listOf(
                R.string.single_answer_1,
                R.string.single_answer_2,
                R.string.single_answer_3
            )
        ),
        description = R.string.single_description
    )
    SkintkerTheme {
        QuestionContent(
            question = question,
            answer = null,
            onAnswer = {},
        )
    }
}

@Preview
@Composable
fun SlideAnswerQuestionPreview() {
    val question = Question(
        id = 3,
        questionText = R.string.slide_answer_title,
        answer = PossibleAnswer.Slider(
            range = 1f..10f,
            steps = 5,
            startText = R.string.slide_point_1,
            endText = R.string.slide_point_1,
            neutralText = R.string.slide_point_1
        ),
        description = R.string.slide_description
    )
    SkintkerTheme {
        QuestionContent(
            question = question,
            answer = null,
            onAnswer = {},
        )
    }
}

@Preview
@Composable
fun DoubleSlideAnswerQuestionPreview() {
    val question = Question(
        id = 4,
        questionText = R.string.slide_answer_title,
        answer = PossibleAnswer.DoubleSlider(
            range = 1f..10f,
            steps = 5,
            defaultValueFirstSlider = 2.0f,
            startTextFirstSlider = R.string.slide_point_1,
            endTextFirstSlider = R.string.slide_point_2,
            defaultValueSecondSlider = 8.0f,
            startTextSecondSlider = R.string.slide_point_1,
            endTextSecondSlider = R.string.slide_point_2,
        ),
        description = R.string.slide_description
    )
    SkintkerTheme {
        QuestionContent(
            question = question,
            answer = null,
            onAnswer = {},
        )
    }
}

@Preview
@Composable
fun SingleTextInputAnswerQuestionPreview() {
    val question = Question(
        id = 5,
        questionText = R.string.slide_answer_title,
        answer = PossibleAnswer.SingleTextInput(
            hint = R.string.slide_point_2,
            maxCharacters = 40
        ),
        description = R.string.slide_description
    )
    SkintkerTheme {
        QuestionContent(
            question = question,
            answer = null,
            onAnswer = {},
        )
    }
}

@Preview
@Composable
fun SingleTextInputSingleChoiceAnswerQuestionPreview() {
    val question = Question(
        id = 5,
        questionText = R.string.slide_answer_title,
        answer = PossibleAnswer.SingleTextInputSingleChoice(
            hint = R.string.slide_point_2,
            maxCharacters = 40,
            optionsStringRes = listOf(
                R.string.single_answer_1,
                R.string.single_answer_2,
                R.string.single_answer_3
            )
        ),
        description = R.string.single_description
    )
    SkintkerTheme {
        QuestionContent(
            question = question,
            answer = null,
            onAnswer = {},
        )
    }
}