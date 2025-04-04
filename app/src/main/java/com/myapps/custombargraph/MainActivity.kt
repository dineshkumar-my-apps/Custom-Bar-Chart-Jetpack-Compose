package com.myapps.custombargraph

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myapps.custombargraph.ui.theme.CustomBarGraphTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CustomBarGraphTheme {
                // A surface container using the 'background' color from the theme
                Column(
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    val dataList = mutableListOf(120,40,50,10,80,90,100)
                    val floatValue = mutableListOf<Float>()
                    val datesList = mutableListOf(1,2,3,4,5,6,7)

                    dataList.forEachIndexed { index, value ->
                        floatValue.add(index = index, element = value.toFloat()/dataList.max().toFloat())
                    }

                    Text(
                        text = "Custom Bar Graph",
                        modifier = Modifier.padding(bottom = 20.dp),
                        fontSize = MaterialTheme.typography.h5.fontSize,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        style = TextStyle(
                            color = Black,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Start,
                        )
                    )

                    Spacer(modifier = Modifier.padding(20.dp))

                    BarGraphWithBarClick(
                        graphBarData = floatValue,
                        xAxisScaleData = datesList,
                        barData_ = dataList,
                        height = 300.dp,
                        roundType = BarType.TOP_CURVED,
                        barWidth = 20.dp,
                        barColor = Color.Blue,
                        barArrangement = Arrangement.SpaceEvenly
                    )
                }
            }
        }
    }
}
