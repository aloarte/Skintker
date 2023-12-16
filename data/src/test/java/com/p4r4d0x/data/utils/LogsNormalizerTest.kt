package com.p4r4d0x.data.utils

import android.content.res.Resources
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.data.R
import com.p4r4d0x.data.parsers.LogsNormalizer
import com.p4r4d0x.data.testutils.appContextModule
import com.p4r4d0x.test.KoinBaseTest
import com.p4r4d0x.test.KoinTestApplication
import io.mockk.every
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.koin.core.component.inject

@RunWith(AndroidJUnit4::class)
@Config(application = KoinTestApplication::class, sdk = [Build.VERSION_CODES.P])
class LogsNormalizerTest : KoinBaseTest(appContextModule) {

    private val resources: Resources by inject()

    private lateinit var logsNormalizer: LogsNormalizer

    @Before
    fun setup() {
        logsNormalizer = LogsNormalizer(resources)
    }

    @Test
    fun testFoodStrings() {
        mockNotNormalizedFoodValues()
        mockNormalizedFoodValues()
        val expectedNormalizedValues = listOf(
            "Meat",
            "Seafood",
            "Blue fish",
            "White fish",
            "Pineapple",
            "Strawberries",
            "Bananas",
            "Citrus",
            "Tomato",
            "Avocado",
            "Eggplant",
            "Zucchini",
            "Pumpkin",
            "Peas",
            "Mushrooms",
            "Eggs",
            "Hot spices",
            "Nuts",
            "Chocolate",
            "Sweets",
            "Dairy products",
            "Fermented dairy",
            "Bread",
            "Pasta",
            "Pickles",
            "Canned legumes",
            "Canned vegetables",
            "Canned fish",
            "Inlay",
            "Sauces",
            "Soy derivatives"
        )
        val questionValues = listOf(
            "Carne 🥩",
            "Mariscos 🦐",
            "Pez azul 🐟",
            "Pez blanco 🐟",
            "Piña 🍍",
            "Fresas 🍓",
            "Plátanos 🍌",
            "Cítricos 🍊",
            "Tomate 🍅",
            "Aguacate 🥑",
            "Berenjena 🍆",
            "Calabacín 🍆",
            "Calabaza 🎃",
            "Guisantes 🌿",
            "Champiñones 🍄",
            "Huevos 🥚",
            "Especias picantes 🌶️",
            "Frutos secos 🥜",
            "Chocolate 🍫",
            "Dulces 🍮",
            "Productos lácteos 🥛",
            "Productos lácteos fermentados 🧀",
            "Pan 🍞",
            "Pasta 🍝",
            "Encurtidos 🥒",
            "Legumbres enlatadas 🥫",
            "Vegetales enlatados 🥫",
            "Pescado enlatado 🥫",
            "Embutidos 🥪",
            "Salsas 🍯",
            "Derivados de soja 🌱"
        )

        for ((index, value) in questionValues.withIndex()) {
            val normalized = logsNormalizer.normalizeFood(value)
            assertEquals(expectedNormalizedValues[index], normalized)
            assertEquals(questionValues[index], logsNormalizer.denormalizeFood(normalized))
        }
    }

    @Test
    fun testZoneNormalization() {
        mockNormalizedZoneValues()
        mockNotNormalizedZoneValues()
        val expectedNormalizedValues = listOf(
            "Ears", "Eyelid", "Cheeks", "Lips", "Neck",
            "Shoulders", "Arms", "Wrists", "Hands", "Legs",
            "Chest", "Back"
        )
        val questionValues = listOf(
            "Oídos",
            "Párpados",
            "Mejillas",
            "Labios",
            "Cuello",
            "Hombros",
            "Brazos",
            "Muñecas",
            "Manos",
            "Piernas",
            "Pecho",
            "Espalda"
        )

        for ((index, value) in questionValues.withIndex()) {
            val normalized = logsNormalizer.normalizeZones(value)
            assertEquals(expectedNormalizedValues[index], normalized)
            assertEquals(questionValues[index], logsNormalizer.denormalizeZones(normalized))
        }
    }

    @Test
    fun testBeerNormalization() {
        mockNormalizedBeerValues()
        mockNotNormalizedBeerValues()
        val expectedNormalizedValues = listOf(
            "Wheat", "Stout", "Porter", "Lager", "Dark Lager",
            "Brown Ale", "Pale Ale/IPA", "Belgian-Style Ale", "Sour Ale"
        )
        val questionValues = listOf(
            "Trigo", "Stout", "Porter", "Lager", "Dark Lager",
            "Brown Ale", "Pale Ale/IPA", "Belgian-Style Ale", "Sour Ale"
        )

        for ((index, value) in questionValues.withIndex()) {
            val normalized = logsNormalizer.normalizeBeers(value)
            assertEquals(expectedNormalizedValues[index], normalized)
            assertEquals(questionValues[index], logsNormalizer.denormalizeBeers(normalized))
        }
    }

    @Test
    fun testWineNormalization() {
        mockNormalizedWineValues()
        mockNotNormalizedWineValues()
        val expectedNormalizedValues =
            listOf("Red", "White", "Rose", "Sparkling", "Vermouth", "Other")
        val questionValues = listOf("Rojo", "Blanco", "Rosado", "Espumoso", "Vermut", "Otro tipo")
        for ((index, value) in questionValues.withIndex()) {
            val normalized = logsNormalizer.normalizeWines(value)
            assertEquals(expectedNormalizedValues[index], normalized)
            assertEquals(questionValues[index], logsNormalizer.denormalizeWines(normalized))
        }
    }

    @Test
    fun testDistilledNormalization() {
        mockNormalizedDistilledValues()
        mockNotNormalizedDistilledValues()
        val expectedNormalizedValues =
            listOf("Gin", "Vodka", "Rum", "Whiskey", "Tequila", "Brandy", "Other")
        val questionValues =
            listOf("Ginebra", "Vodka", "Ron", "Whiskey", "Tequila", "Brand", "Otro")

        for ((index, value) in questionValues.withIndex()) {
            val normalized = logsNormalizer.normalizeDistilledDrinks(value)
            assertEquals(expectedNormalizedValues[index], normalized)
            assertEquals(questionValues[index], logsNormalizer.denormalizeDistilledDrinks(normalized))
        }
    }

    private fun mockNormalizedFoodValues() {
        every { resources.getString(R.string.normalized_question_10_answer_1) } returns "Meat"
        every { resources.getString(R.string.normalized_question_10_answer_2) } returns "Seafood"
        every { resources.getString(R.string.normalized_question_10_answer_3) } returns "Blue fish"
        every { resources.getString(R.string.normalized_question_10_answer_4) } returns "White fish"
        every { resources.getString(R.string.normalized_question_10_answer_5) } returns "Pineapple"
        every { resources.getString(R.string.normalized_question_10_answer_6) } returns "Strawberries"
        every { resources.getString(R.string.normalized_question_10_answer_7) } returns "Bananas"
        every { resources.getString(R.string.normalized_question_10_answer_8) } returns "Citrus"
        every { resources.getString(R.string.normalized_question_10_answer_9) } returns "Tomato"
        every { resources.getString(R.string.normalized_question_10_answer_10) } returns "Avocado"
        every { resources.getString(R.string.normalized_question_10_answer_11) } returns "Eggplant"
        every { resources.getString(R.string.normalized_question_10_answer_12) } returns "Zucchini"
        every { resources.getString(R.string.normalized_question_10_answer_13) } returns "Pumpkin"
        every { resources.getString(R.string.normalized_question_10_answer_14) } returns "Peas"
        every { resources.getString(R.string.normalized_question_10_answer_15) } returns "Mushrooms"
        every { resources.getString(R.string.normalized_question_10_answer_16) } returns "Eggs"
        every { resources.getString(R.string.normalized_question_10_answer_17) } returns "Hot spices"
        every { resources.getString(R.string.normalized_question_10_answer_18) } returns "Nuts"
        every { resources.getString(R.string.normalized_question_11_answer_1) } returns "Chocolate"
        every { resources.getString(R.string.normalized_question_11_answer_2) } returns "Sweets"
        every { resources.getString(R.string.normalized_question_11_answer_3) } returns "Dairy products"
        every { resources.getString(R.string.normalized_question_11_answer_4) } returns "Fermented dairy"
        every { resources.getString(R.string.normalized_question_11_answer_5) } returns "Bread"
        every { resources.getString(R.string.normalized_question_11_answer_6) } returns "Pasta"
        every { resources.getString(R.string.normalized_question_11_answer_7) } returns "Pickles"
        every { resources.getString(R.string.normalized_question_11_answer_8) } returns "Canned legumes"
        every { resources.getString(R.string.normalized_question_11_answer_9) } returns "Canned vegetables"
        every { resources.getString(R.string.normalized_question_11_answer_10) } returns "Canned fish"
        every { resources.getString(R.string.normalized_question_11_answer_11) } returns "Inlay"
        every { resources.getString(R.string.normalized_question_11_answer_12) } returns "Sauces"
        every { resources.getString(R.string.normalized_question_11_answer_13) } returns "Soy derivatives"
    }

    private fun mockNormalizedZoneValues() {
        every { resources.getString(R.string.normalized_question_2_answer_1) } returns "Ears"
        every { resources.getString(R.string.normalized_question_2_answer_2) } returns "Eyelid"
        every { resources.getString(R.string.normalized_question_2_answer_3) } returns "Cheeks"
        every { resources.getString(R.string.normalized_question_2_answer_4) } returns "Lips"
        every { resources.getString(R.string.normalized_question_2_answer_5) } returns "Neck"
        every { resources.getString(R.string.normalized_question_2_answer_6) } returns "Shoulders"
        every { resources.getString(R.string.normalized_question_2_answer_7) } returns "Arms"
        every { resources.getString(R.string.normalized_question_2_answer_8) } returns "Wrists"
        every { resources.getString(R.string.normalized_question_2_answer_9) } returns "Hands"
        every { resources.getString(R.string.normalized_question_2_answer_10) } returns "Legs"
        every { resources.getString(R.string.normalized_question_2_answer_11) } returns "Chest"
        every { resources.getString(R.string.normalized_question_2_answer_12) } returns "Back"
    }

    private fun mockNormalizedBeerValues() {
        every { resources.getString(R.string.normalized_question_5_answer_1) } returns "Wheat"
        every { resources.getString(R.string.normalized_question_5_answer_2) } returns "Stout"
        every { resources.getString(R.string.normalized_question_5_answer_3) } returns "Porter"
        every { resources.getString(R.string.normalized_question_5_answer_4) } returns "Lager"
        every { resources.getString(R.string.normalized_question_5_answer_5) } returns "Dark Lager"
        every { resources.getString(R.string.normalized_question_5_answer_6) } returns "Brown Ale"
        every { resources.getString(R.string.normalized_question_5_answer_7) } returns "Pale Ale/IPA"
        every { resources.getString(R.string.normalized_question_5_answer_8) } returns "Belgian-Style Ale"
        every { resources.getString(R.string.normalized_question_5_answer_9) } returns "Sour Ale"
        every { resources.getString(R.string.normalized_question_5_answer_10) } returns "Other"
    }

    private fun mockNormalizedWineValues() {
        every { resources.getString(R.string.normalized_question_6_answer_1) } returns "Red"
        every { resources.getString(R.string.normalized_question_6_answer_2) } returns "White"
        every { resources.getString(R.string.normalized_question_6_answer_3) } returns "Rose"
        every { resources.getString(R.string.normalized_question_6_answer_4) } returns "Sparkling"
        every { resources.getString(R.string.normalized_question_6_answer_5) } returns "Vermouth"
        every { resources.getString(R.string.normalized_question_6_answer_6) } returns "Other"
    }

    private fun mockNormalizedDistilledValues() {
        every { resources.getString(R.string.normalized_question_7_answer_1) } returns "Gin"
        every { resources.getString(R.string.normalized_question_7_answer_2) } returns "Vodka"
        every { resources.getString(R.string.normalized_question_7_answer_3) } returns "Rum"
        every { resources.getString(R.string.normalized_question_7_answer_4) } returns "Whiskey"
        every { resources.getString(R.string.normalized_question_7_answer_5) } returns "Tequila"
        every { resources.getString(R.string.normalized_question_7_answer_6) } returns "Brandy"
        every { resources.getString(R.string.normalized_question_7_answer_7) } returns "Other"
    }

    private fun mockNotNormalizedFoodValues() {
        every { resources.getString(R.string.question_10_answer_1) } returns "Carne 🥩"
        every { resources.getString(R.string.question_10_answer_2) } returns "Mariscos 🦐"
        every { resources.getString(R.string.question_10_answer_3) } returns "Pez azul 🐟"
        every { resources.getString(R.string.question_10_answer_4) } returns "Pez blanco 🐟"
        every { resources.getString(R.string.question_10_answer_5) } returns "Piña 🍍"
        every { resources.getString(R.string.question_10_answer_6) } returns "Fresas 🍓"
        every { resources.getString(R.string.question_10_answer_7) } returns "Plátanos 🍌"
        every { resources.getString(R.string.question_10_answer_8) } returns "Cítricos 🍊"
        every { resources.getString(R.string.question_10_answer_9) } returns "Tomate 🍅"
        every { resources.getString(R.string.question_10_answer_10) } returns "Aguacate 🥑"
        every { resources.getString(R.string.question_10_answer_11) } returns "Berenjena 🍆"
        every { resources.getString(R.string.question_10_answer_12) } returns "Calabacín 🍆"
        every { resources.getString(R.string.question_10_answer_13) } returns "Calabaza 🎃"
        every { resources.getString(R.string.question_10_answer_14) } returns "Guisantes 🌿"
        every { resources.getString(R.string.question_10_answer_15) } returns "Champiñones 🍄"
        every { resources.getString(R.string.question_10_answer_16) } returns "Huevos 🥚"
        every { resources.getString(R.string.question_10_answer_17) } returns "Especias picantes 🌶️"
        every { resources.getString(R.string.question_10_answer_18) } returns "Frutos secos 🥜"
        every { resources.getString(R.string.question_11_answer_1) } returns "Chocolate 🍫"
        every { resources.getString(R.string.question_11_answer_2) } returns "Dulces 🍮"
        every { resources.getString(R.string.question_11_answer_3) } returns "Productos lácteos 🥛"
        every { resources.getString(R.string.question_11_answer_4) } returns "Productos lácteos fermentados 🧀"
        every { resources.getString(R.string.question_11_answer_5) } returns "Pan 🍞"
        every { resources.getString(R.string.question_11_answer_6) } returns "Pasta 🍝"
        every { resources.getString(R.string.question_11_answer_7) } returns "Encurtidos 🥒"
        every { resources.getString(R.string.question_11_answer_8) } returns "Legumbres enlatadas 🥫"
        every { resources.getString(R.string.question_11_answer_9) } returns "Vegetales enlatados 🥫"
        every { resources.getString(R.string.question_11_answer_10) } returns "Pescado enlatado 🥫"
        every { resources.getString(R.string.question_11_answer_11) } returns "Embutidos 🥪"
        every { resources.getString(R.string.question_11_answer_12) } returns "Salsas 🍯"
        every { resources.getString(R.string.question_11_answer_13) } returns "Derivados de soja 🌱"
    }

    private fun mockNotNormalizedZoneValues() {
        every { resources.getString(R.string.question_2_answer_1) } returns "Oídos"
        every { resources.getString(R.string.question_2_answer_2) } returns "Párpados"
        every { resources.getString(R.string.question_2_answer_3) } returns "Mejillas"
        every { resources.getString(R.string.question_2_answer_4) } returns "Labios"
        every { resources.getString(R.string.question_2_answer_5) } returns "Cuello"
        every { resources.getString(R.string.question_2_answer_6) } returns "Hombros"
        every { resources.getString(R.string.question_2_answer_7) } returns "Brazos"
        every { resources.getString(R.string.question_2_answer_8) } returns "Muñecas"
        every { resources.getString(R.string.question_2_answer_9) } returns "Manos"
        every { resources.getString(R.string.question_2_answer_10) } returns "Piernas"
        every { resources.getString(R.string.question_2_answer_11) } returns "Pecho"
        every { resources.getString(R.string.question_2_answer_12) } returns "Espalda"
    }

    private fun mockNotNormalizedBeerValues() {
        every { resources.getString(R.string.question_5_answer_1) } returns "Trigo"
        every { resources.getString(R.string.question_5_answer_2) } returns "Stout"
        every { resources.getString(R.string.question_5_answer_3) } returns "Porter"
        every { resources.getString(R.string.question_5_answer_4) } returns "Lager"
        every { resources.getString(R.string.question_5_answer_5) } returns "Dark Lager"
        every { resources.getString(R.string.question_5_answer_6) } returns "Brown Ale"
        every { resources.getString(R.string.question_5_answer_7) } returns "Pale Ale/IPA"
        every { resources.getString(R.string.question_5_answer_8) } returns "Belgian-Style Ale"
        every { resources.getString(R.string.question_5_answer_9) } returns "Sour Ale"
    }

    private fun mockNotNormalizedWineValues() {
        every { resources.getString(R.string.question_6_answer_1) } returns "Rojo"
        every { resources.getString(R.string.question_6_answer_2) } returns "Blanco"
        every { resources.getString(R.string.question_6_answer_3) } returns "Rosado"
        every { resources.getString(R.string.question_6_answer_4) } returns "Espumoso"
        every { resources.getString(R.string.question_6_answer_5) } returns "Vermut"
        every { resources.getString(R.string.question_6_answer_6) } returns "Otro tipo"
    }

    private fun mockNotNormalizedDistilledValues() {
        every { resources.getString(R.string.question_7_answer_1) } returns "Ginebra"
        every { resources.getString(R.string.question_7_answer_2) } returns "Vodka"
        every { resources.getString(R.string.question_7_answer_3) } returns "Ron"
        every { resources.getString(R.string.question_7_answer_4) } returns "Whiskey"
        every { resources.getString(R.string.question_7_answer_5) } returns "Tequila"
        every { resources.getString(R.string.question_7_answer_6) } returns "Brand"
        every { resources.getString(R.string.question_7_answer_7) } returns "Otro"
    }

}