package com.example.iyengaryoga20.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun AboutScreen(navController: NavController? = null) {
    val scrollState = rememberScrollState()

    // Главный контейнер (БЕЗ скролла)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // --- 1. ЗАКРЕПЛЕННАЯ ШАПКА СО СТРЕЛОЧКОЙ НАЗАД ---

        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 8.dp, top = 16.dp, bottom = 16.dp, end = 16.dp)
            ) {
                IconButton(onClick = { navController?.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Назад", tint = MaterialTheme.colorScheme.onBackground)
                }
                Text(
                    text = "О центре",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        // --- 2. СКРОЛЛИРУЕМЫЙ КОНТЕНТ ---
        // Текст будет прокручиваться под шапкой
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp) // Отступы только по бокам
        ) {
            // "Герой"-баннер сверху
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp)),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.SelfImprovement,
                        contentDescription = null,
                        modifier = Modifier.size(90.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- БЛОК: ИСТОРИЯ ЦЕНТРА ---
            SectionTitle("История Уральского центра йоги Айенгара")
            ParagraphText("Уральский центр Айенгар йоги — первый на Урале лицензированный центр, который стал работать по методу Айенгара. Егор Ротов, основатель Центра, получил благословение на открытие лично от Гуруджи Шри Б.К.С. Айенгара.")

            Spacer(modifier = Modifier.height(32.dp))
            Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(32.dp))

            // --- ГЛАВНЫЙ ЗАГОЛОВОК ПРО АЙЕНГАРА ---
            Text(
                text = "Гуруджи Б.К.С. Айенгар и его метод",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- ЧАСТЬ 1: О ЙОГЕ ---
            SectionTitle("О йоге Айенгара")
            ParagraphText("Йога Айенгара – это классическая, традиционная хатха-йога. Метод получил свое название в честь Беллура Кришнамачарьи Сундараджи Айенгара, великого мастера йоги, проживавшего в Индии (1918–2014).")
            ParagraphText("Йога Айенгара – классическая йога, так как в основе ее лежит философская система Патанджали, мудреца жившего более двух тысяч лет назад, который систематизировал учение йоги.")
            ParagraphText("Йога Айенгара – традиционная йога, так как восходит к традиции Тирумалаи Кришнамачарьи, имеет линию преемственности, парампару.")
            ParagraphText("Благодаря своему личному опыту, упорной, настойчивой практике и наблюдению Б.К.С. Айенгар разработал метод, сделавшей йогу доступной всем желающим практиковать, улучшать свое самочувствие, справляться с болезнями и недомоганиями, противостоять стрессам.")
            ParagraphText("Это одно из самых распространенных направлений йоги в современном мире.")

            Spacer(modifier = Modifier.height(24.dp))

            // --- ЧАСТЬ 2: ОСОБЕННОСТИ МЕТОДА ---
            SectionTitle("Особенности метода")
            ParagraphText("В основе метода лежат три фундаментальных аспекта:")

            Spacer(modifier = Modifier.height(8.dp))

            FeatureCard("1. Точность", "В практике асан большое внимание уделяется точности выстраивания каждой позы (асаны), ее корректному выполнению, достижению симметрии.")
            FeatureCard("2. Время", "Имеет значение не только то, как выполняется асана (точность), но и время пребывания в ней. Тонкая, глубокая работа в позах требует более длительного времени пребывания в них, что усиливает положительный эффект от практики, развивает способность к концентрации и медитации.")
            FeatureCard("3. Последовательность", "Асаны выполняются в определенной последовательности, в зависимости от задач, которые необходимо решить с помощью практики.")

            Spacer(modifier = Modifier.height(8.dp))
            ParagraphText("Все три аспекта неразрывно связаны друг с другом. Благодаря их сочетанию практика воздействует не только на физическое и физиологическое тело, но и на психоэмоциональное состояние, состояние ума, развивает концентрацию и осознанность.")

            Spacer(modifier = Modifier.height(32.dp))

            // --- ЧАСТЬ 3: ВЕХИ ЖИЗНИ ---
            SectionTitle("Вехи жизненного пути Б.К.С. Айенгара")

            MilestoneText("Беллур", "Беллур Кришнамачар Сундараджи Айенгар родился 14 декабря 1918 г. в деревне Беллур, штат Карнатака в обедневшей семье браминов. В то время мир охватила пандемии гриппа. Его мать Шешамма во время беременности переболела гриппом и у мальчика с самого рождения было слабое здоровье. Он бесконечно болел, перенес малярию, тиф, туберкулез. После смерти отца семья оказалась в страшной нищете. Слабое здоровье сказывалось на его успеваемости в школе. В 1936 году, провалившись на выпускных экзаменах его обучение прекратилось.")
            MilestoneText("Майсор", "В возрасте 16 лет (в марте 1934 г.) он начал обучаться йоге у великого мастера Т. Кришнамачарьи, мужа его старшей сестры, который взялся обучать его асанам, чтобы поправить здоровье. Однако обучение у Кришнамачарьи было жестким и нерегулярным, приходилось за небольшой срок осваивать и демонстрировать сложные позы. В результате, им были получены травмы связок и суставов. В 1936 г. Б.К.С. Айенгар начинает преподавать йогу.")
            MilestoneText("Пуна", "В 1937 г. Б.К.С. Айенгар по настоянию своего гуру отправляется в Пуну преподавать йогу. Занимается по 10 часов в день, чтобы напрямую получить знания на собственном опыте.")
            MilestoneText("Рамамани", "В 1943 г. Айенгар женился на Рамамани, ставшей преданной спутницей жизни, матерью его шестерых детей. Прашант и Гита Айенгары посвятили свою жизнь йоге и стали всемирно известными учителями.")
            MilestoneText("Ученики", "Количество учеников росло. Обучаться стали приезжать европейцы. Судьбоносной стала встреча со скрипачом Иегуди Менухином в 1952 г. По его приглашению Айенгар отправляется в турне по Европе.")
            MilestoneText("Институт", "В 1975 г. было завершено строительство института йоги в Пуне, куда съезжаются ученики со всего мира.")
            MilestoneText("Книги", "Б.К.С. Айенгар написал множество книг. «Прояснение йоги» (1966 г.) считается «библией йоги» и переведена на несколько десятков языков.")
            MilestoneText("Мир", "В 1954 г. посещает страны Европы, в 1956 г. – США. Благодаря Б.К.С. Айенгару йога стала широко известна на Западе и получила распространение по всему миру.")
            MilestoneText("Россия", "Б.К.С. Айенгар посетил нашу страну дважды. В 1989 г. он провел два класса для 1000 участников, положив начало развитию йоги Айенгара в России. Второй визит состоялся в 2009 г. – конференцию посетило около 2000 человек.")

            Spacer(modifier = Modifier.height(16.dp))

            // Финальные факты
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    BulletText("В 2004 г. журнал «Time» включил Б.К.С. Айенгара в список 100 самых влиятельных людей мира.")
                    BulletText("В 2014 г. Б.К.С. Айенгар был номинирован на Нобелевскую премию мира.")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Великий мастер ушел из жизни 20 августа 2014 г. и оставил великое наследие, дар всему человечеству – йогу, практика которой стала доступной для всех желающих практиковать вне зависимости от возраста и физических возможностей.",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- КНОПКА ПЕРЕХОДА К ПРЕПОДАВАТЕЛЯМ ---
            Button(
                onClick = { navController?.navigate("teachers") },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Наша команда преподавателей", color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

// --- ВСПОМОГАТЕЛЬНЫЕ КОМПОНЕНТЫ ---

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(bottom = 12.dp)
    )
}

@Composable
fun ParagraphText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        lineHeight = 22.sp,
        modifier = Modifier.padding(bottom = 10.dp)
    )
}

@Composable
fun MilestoneText(title: String, text: String) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)) {
                append("$title. ")
            }
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
                append(text)
            }
        },
        style = MaterialTheme.typography.bodyMedium,
        lineHeight = 22.sp,
        modifier = Modifier.padding(bottom = 12.dp)
    )
}

@Composable
fun BulletText(text: String) {
    Row(modifier = Modifier.padding(bottom = 8.dp)) {
        Text("•", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 8.dp))
        Text(text = text, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 20.sp)
    }
}

@Composable
fun FeatureCard(title: String, desc: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = desc,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )
        }
    }
}