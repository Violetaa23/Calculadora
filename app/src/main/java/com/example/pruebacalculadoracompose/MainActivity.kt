import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pruebacalculadoracompose.ui.theme.PruebaCalculadoracomposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PruebaCalculadoracomposeTheme {
                CalculatorScreen()
            }
        }
    }
}

@Composable
fun CalculatorScreen() {
    // Estado para mostrar la pantalla de resultados
    var display by remember { mutableStateOf("0") }

    // Estado para la operación y los números actuales
    var number1 by remember { mutableStateOf("") }
    var number2 by remember { mutableStateOf("") }
    var operator by remember { mutableStateOf("") }

    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT

    if (isPortrait) {
        // Layout vertical
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1E1E1E)) // Fondo oscuro
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CalculatorDisplay(display)
            Spacer(modifier = Modifier.height(16.dp))
            CalculatorButtons(
                onButtonClick = { buttonText ->
                    onButtonClick(
                        buttonText, display, { display = it },
                        { number1 = it }, { number2 = it }, { operator = it },
                        number1, number2, operator
                    )
                }
            )
        }
    } else {
        // Layout horizontal
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1E1E1E)) // Fondo oscuro
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(modifier = Modifier.weight(1f)) {
                CalculatorDisplay(display)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                CalculatorButtons(
                    onButtonClick = { buttonText ->
                        onButtonClick(
                            buttonText, display, { display = it },
                            { number1 = it }, { number2 = it }, { operator = it },
                            number1, number2, operator
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun CalculatorDisplay(display: String) {
    Text(
        text = display,
        fontSize = 48.sp,
        color = Color.White, // Color del texto en blanco
        modifier = Modifier.fillMaxWidth(),
        maxLines = 1
    )
}

@Composable
fun CalculatorButtons(onButtonClick: (String) -> Unit) {
    val buttons = listOf(
        listOf("7", "8", "9", "/"),
        listOf("4", "5", "6", "*"),
        listOf("1", "2", "3", "-"),
        listOf("0", "C", "=", "+")
    )

    for (row in buttons) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (buttonText in row) {
                val isOperator = buttonText == "/" || buttonText == "*" || buttonText == "-" || buttonText == "+" || buttonText == "=" || buttonText == "C"
                Button(
                    onClick = { onButtonClick(buttonText) },
                    modifier = Modifier
                        .padding(4.dp)
                        .size(72.dp),  // Botones cuadrados
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isOperator) Color(0xFFD1C4E9) else Color(0xFFBDBDBD) // Morado claro para operadores, gris oscuro para números
                    ),
                    shape = RoundedCornerShape(0.dp) // Forma cuadrada
                ) {
                    Text(
                        text = buttonText,
                        fontSize = 24.sp,
                        color = Color.Black // Texto en negro para mejor contraste
                    )
                }
            }
        }
    }
}

fun onButtonClick(
    buttonText: String,
    display: String,
    updateDisplay: (String) -> Unit,
    updateNumber1: (String) -> Unit,
    updateNumber2: (String) -> Unit,
    updateOperator: (String) -> Unit,
    number1: String,
    number2: String,
    operator: String
) {
    when (buttonText) {
        "C" -> {
            updateDisplay("0")
            updateNumber1("")
            updateNumber2("")
            updateOperator("")
        }
        "=" -> {
            if (number1.isNotEmpty() && number2.isNotEmpty() && operator.isNotEmpty()) {
                val result = calculateResult(number1, number2, operator)
                updateDisplay(result)
                updateNumber1(result)
                updateNumber2("")
                updateOperator("")
            }
        }
        "+", "-", "*", "/" -> {
            if (number1.isNotEmpty()) {
                updateOperator(buttonText)
                updateDisplay("$number1 $buttonText")
            }
        }
        else -> {
            if (operator.isEmpty()) {
                val newNumber1 = number1 + buttonText
                updateNumber1(newNumber1)
                updateDisplay(newNumber1)
            } else {
                val newNumber2 = number2 + buttonText
                updateNumber2(newNumber2)
                updateDisplay("$number1 $operator $newNumber2")
            }
        }
    }
}

fun calculateResult(number1: String, number2: String, operator: String): String {
    return try {
        val num1 = number1.toDouble()
        val num2 = number2.toDouble()
        when (operator) {
            "+" -> (num1 + num2).toString()
            "-" -> (num1 - num2).toString()
            "*" -> (num1 * num2).toString()
            "/" -> (num1 / num2).toString()
            else -> "Error"
        }
    } catch (e: Exception) {
        "Error"
    }
}
