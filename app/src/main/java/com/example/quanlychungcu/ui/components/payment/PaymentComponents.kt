package com.example.quanlychungcu.ui.components.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quanlychungcu.data.model.PaymentHistoryItem
import com.example.quanlychungcu.data.model.UnpaidBill
import com.example.quanlychungcu.ui.theme.*

@Composable
fun PaymentCardSection(
    title: String,
    icon: ImageVector,
    iconColor: Color,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp, RoundedCornerShape(16.dp))
            .background(White, RoundedCornerShape(16.dp))
            .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 16.dp)) {
            Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text(title, fontWeight = FontWeight.Bold, color = TextDark, fontSize = 16.sp)
        }
        content()
    }
}

@Composable
fun UnpaidBillCard(bill: UnpaidBill) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(RedBg, RoundedCornerShape(12.dp))
            .border(1.dp, RedBorder, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Đợt thu", fontSize = 12.sp, color = RedTextSub)
                Text(bill.period, fontWeight = FontWeight.Bold, color = RedTextMain, fontSize = 14.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("Hạn thanh toán", fontSize = 12.sp, color = RedTextSub)
                Text(bill.dueDate, fontWeight = FontWeight.Bold, color = RedTextSub, fontSize = 14.sp)
            }
        }
        HorizontalDivider(color = RedBorder, thickness = 1.dp)
        Column(modifier = Modifier.padding(16.dp)) {
            bill.details.forEach { detail ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(detail.name, fontSize = 14.sp, color = TextDark, fontWeight = FontWeight.Medium)
                    Text(detail.price, fontSize = 14.sp, color = TextDark, fontWeight = FontWeight.SemiBold)
                }
                HorizontalDivider(color = RedBorder.copy(alpha = 0.5f), thickness = 1.dp, modifier = Modifier.padding(bottom = 8.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text("Tổng thanh toán", fontSize = 14.sp, color = RedTextSub, fontWeight = FontWeight.Medium)
                Text(bill.amount, fontSize = 20.sp, color = RedTextMain, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* Handle Payment */ },
                colors = ButtonDefaults.buttonColors(containerColor = RedButton),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Icon(Icons.Outlined.CreditCard, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Thanh toán ngay", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ITEM LỊCH SỬ (CÓ DẤU CHẤM TIMELINE)
@Composable
fun PaymentHistoryRow(
    item: PaymentHistoryItem,
    isLast: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clickable { onClick() }
    ) {
        // Cột Timeline
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(24.dp)
                .fillMaxHeight()
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 6.dp)
                    .size(10.dp)
                    .background(IndigoPrimary, CircleShape)
            )
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight()
                        .background(IndigoPrimary.copy(alpha = 0.2f))
                        .padding(top = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Cột Nội dung
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        item.month,
                        color = IndigoPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                    Text(
                        item.id,
                        fontSize = 12.sp,
                        color = TextGray,
                        fontWeight = FontWeight.Medium
                    )
                }
                Text(
                    item.amount,
                    color = GreenText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }

            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .background(BgColor, RoundedCornerShape(8.dp))
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.AccessTime, contentDescription = null, tint = TextLightGray, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(item.time, fontSize = 12.sp, color = TextLightGray)

                Spacer(modifier = Modifier.width(16.dp))

                Text("Ngày:", fontSize = 12.sp, color = TextLightGray)
                Spacer(modifier = Modifier.width(4.dp))
                Text(item.paymentDate, fontSize = 12.sp, color = TextDark, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
fun PaymentSupportBox(
    bgColor: Color,
    borderColor: Color,
    icon: ImageVector,
    iconColor: Color,
    label: String,
    value: String,
    valueColor: Color,
    subText: String? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor, RoundedCornerShape(12.dp))
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(White, CircleShape)
                .shadow(1.dp, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = iconColor, modifier = Modifier.size(20.dp))
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(label, fontSize = 13.sp, color = TextGray)
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = valueColor)
            if (subText != null) {
                Text(subText, fontSize = 12.sp, color = TextGray, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
fun DetailRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = TextGray, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(label, fontSize = 14.sp, color = TextGray)
        }
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextDark, textAlign = TextAlign.End, modifier = Modifier.fillMaxWidth(0.6f))
    }
}