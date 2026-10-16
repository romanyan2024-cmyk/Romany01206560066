package com.example.data.db

import com.example.data.dao.AppDao
import com.example.data.model.AreaEntity
import com.example.data.model.ChildEntity
import com.example.data.model.ClassEntity
import com.example.data.model.GovernorateEntity
import com.example.data.model.SupervisorAreaAssignmentEntity
import com.example.data.model.SupervisorEntity
import com.example.data.model.TeacherEntity

object SeedData {
  suspend fun populateInitialData(dao: AppDao) {
    // 1. Governorates
    val governorates = listOf(
      GovernorateEntity(id = "minya", name = "محافظة المنيا", orderIndex = 1),
      GovernorateEntity(id = "beni_suef", name = "محافظة بني سويف", orderIndex = 2)
    )
    dao.insertGovernorates(governorates)

    // 2. Areas in specified order
    val areas = listOf(
      // Minya areas (8)
      AreaEntity(id = "abu_qurqas", governorateId = "minya", name = "أبوقرقاص", orderIndex = 1),
      AreaEntity(id = "minya_city", governorateId = "minya", name = "المنيا", orderIndex = 2),
      AreaEntity(id = "beni_mazar", governorateId = "minya", name = "بني مزار", orderIndex = 3),
      AreaEntity(id = "dair_mawas", governorateId = "minya", name = "دير مواس", orderIndex = 4),
      AreaEntity(id = "samalut", governorateId = "minya", name = "سمالوط", orderIndex = 5),
      AreaEntity(id = "matay", governorateId = "minya", name = "مطاي", orderIndex = 6),
      AreaEntity(id = "maghagha", governorateId = "minya", name = "مغاغة", orderIndex = 7),
      AreaEntity(id = "mallawi", governorateId = "minya", name = "ملوي", orderIndex = 8),

      // Beni Suef areas (3)
      AreaEntity(id = "fashn", governorateId = "beni_suef", name = "الفشن", orderIndex = 9),
      AreaEntity(id = "beba", governorateId = "beni_suef", name = "ببا", orderIndex = 10),
      AreaEntity(id = "samasta", governorateId = "beni_suef", name = "سمسطا", orderIndex = 11)
    )
    dao.insertAreas(areas)

    // 3. Supervisors
    val supervisors = listOf(
      SupervisorEntity(
        id = "sup_1",
        name = "أ. مينا سمير غالي",
        phone = "01223456781",
        email = "mina.samir@leaders2026.org",
        governorateId = "minya",
        notes = "المشرف العام لقطاع شمال المنيا (مغاغة وبني مزار ومطاي)"
      ),
      SupervisorEntity(
        id = "sup_2",
        name = "د. بيتر ناصف رزق",
        phone = "01098765432",
        email = "peter.nassef@leaders2026.org",
        governorateId = "minya",
        notes = "مشرف قطاع وسط وجنوب المنيا (مدينة المنيا، أبوقرقاص، ملوي)"
      ),
      SupervisorEntity(
        id = "sup_3",
        name = "أ. تريزا يوسف وهبة",
        phone = "01145678901",
        email = "teresa.youssef@leaders2026.org",
        governorateId = "minya",
        notes = "مشرفة المتابعة التربوية لسمالوط ودير مواس"
      ),
      SupervisorEntity(
        id = "sup_4",
        name = "أ. هاني شكري كامل",
        phone = "01287654321",
        email = "hany.shokry@leaders2026.org",
        governorateId = "beni_suef",
        notes = "المشرف العام لمحافظة بني سويف (الفشن وببا وسمسطا)"
      ),
      SupervisorEntity(
        id = "sup_5",
        name = "أ. ماريان عاطف حبيب",
        phone = "01012349876",
        email = "marianne.atef@leaders2026.org",
        governorateId = "beni_suef",
        notes = "مشرفة التقييم والجودة وتدريب الخدام - بني سويف"
      )
    )
    dao.insertSupervisors(supervisors)

    // 4. Assignments
    val assignments = listOf(
      SupervisorAreaAssignmentEntity(supervisorId = "sup_1", areaId = "maghagha", roleTitle = "مشرف قطاع شمال"),
      SupervisorAreaAssignmentEntity(supervisorId = "sup_1", areaId = "beni_mazar", roleTitle = "مشرف قطاع شمال"),
      SupervisorAreaAssignmentEntity(supervisorId = "sup_1", areaId = "matay", roleTitle = "مشرف قطاع شمال"),
      SupervisorAreaAssignmentEntity(supervisorId = "sup_2", areaId = "minya_city", roleTitle = "مشرف رئيسي"),
      SupervisorAreaAssignmentEntity(supervisorId = "sup_2", areaId = "abu_qurqas", roleTitle = "مشرف متابعة"),
      SupervisorAreaAssignmentEntity(supervisorId = "sup_2", areaId = "mallawi", roleTitle = "مشرف متابعة"),
      SupervisorAreaAssignmentEntity(supervisorId = "sup_3", areaId = "samalut", roleTitle = "مشرفة تربوية"),
      SupervisorAreaAssignmentEntity(supervisorId = "sup_3", areaId = "dair_mawas", roleTitle = "مشرفة تربوية"),
      SupervisorAreaAssignmentEntity(supervisorId = "sup_4", areaId = "fashn", roleTitle = "مشرف عام بني سويف"),
      SupervisorAreaAssignmentEntity(supervisorId = "sup_4", areaId = "beba", roleTitle = "مشرف عام بني سويف"),
      SupervisorAreaAssignmentEntity(supervisorId = "sup_5", areaId = "samasta", roleTitle = "مشرفة تقييم وجودة"),
      SupervisorAreaAssignmentEntity(supervisorId = "sup_5", areaId = "beba", roleTitle = "مشرفة أنشطة")
    )
    dao.insertAssignments(assignments)

    // 5. Teachers / Servants
    val teachers = listOf(
      TeacherEntity("t_1", "جون رأفت لبيب", "01201112233", "minya_city", "خادم مرحلة ابتدائي وبناء مهارات", "خبرة ٦ سنوات"),
      TeacherEntity("t_2", "سارة عادل توفيق", "01002223344", "minya_city", "مسؤولة الأنشطة التفاعلية والفنون", "خريجة تربية"),
      TeacherEntity("t_3", "فادي إبراهيم حنا", "01103334455", "abu_qurqas", "مدرس مهارات القيادة والخطابة", "معلم لغات"),
      TeacherEntity("t_4", "مارتينا مجدي صبحي", "01204445566", "abu_qurqas", "خادمة مرحلة الطفولة المبكرة", "أخصائية تعديل سلوك"),
      TeacherEntity("t_5", "أمير سمير فايز", "01005556677", "mallawi", "مدرس تدريب وتنمية قدرات التفكير", "مهندس برمجيات"),
      TeacherEntity("t_6", "كريستين شريف لمعي", "01106667788", "mallawi", "مسؤولة ورش عمل الإبداع", "مدربة معتمدة"),
      TeacherEntity("t_7", "كيرلس نادي مسعود", "01207778899", "samalut", "خادم مرحلة إعدادي وبناء الفرق", "معلم علوم"),
      TeacherEntity("t_8", "ريهام نبيل بشارة", "01008889900", "samalut", "خادمة مهارات الذكاء العاطفي", "أخصائية نفسية"),
      TeacherEntity("t_9", "مايكل صفوت غطاس", "01109990011", "beni_mazar", "مدرس القيم والأخلاقيات القيادية", "معلم لغة عربية"),
      TeacherEntity("t_10", "مارينا كمال بطرس", "01211112233", "matay", "مسؤولة المناظرات وتحدث الجمهور", "محامية"),
      TeacherEntity("t_11", "مارك وجدي فريد", "01012223344", "maghagha", "خادم مشاريع الخدمة المجتمعية", "أخصائي اجتماعي"),
      TeacherEntity("t_12", "إنجي ملاك نجيب", "01113334455", "dair_mawas", "خادمة الابتكار والعلوم المبسطة", "صيدلانية"),
      TeacherEntity("t_13", "بولا إيليا حكيم", "01214445566", "fashn", "مدرس التربية القيادية والأنشطة", "مدرب كشفي"),
      TeacherEntity("t_14", "ميرنا طلعت رمزي", "01015556677", "beba", "خادمة السلوك الإيجابي وبناء الثقة", "معلمة رياض أطفال"),
      TeacherEntity("t_15", "يوسف جورج سوريال", "01116667788", "samasta", "خادم مرحلة ابتدائية وورش عمل", "محاسب")
    )
    dao.insertTeachers(teachers)

    // 6. Classes
    val classes = listOf(
      ClassEntity("c_1", "فصل فرسان النور (أولى وتانية)", "minya_city", "ابتدائي مبكر", "t_1", 25),
      ClassEntity("c_2", "فصل صناع الأمل (تالتة ورابعة)", "minya_city", "ابتدائي متقدم", "t_2", 28),
      ClassEntity("c_3", "فصل قادة المستقبل (خامسة وسادسة)", "abu_qurqas", "ابتدائي متقدم", "t_3", 30),
      ClassEntity("c_4", "فصل براعم المحبة (أولى وتانية)", "abu_qurqas", "ابتدائي مبكر", "t_4", 22),
      ClassEntity("c_5", "فصل منارة الجنوب (إعدادي قادة)", "mallawi", "إعدادي", "t_5", 30),
      ClassEntity("c_6", "فصل زهور الأمل (تالتة ورابعة)", "mallawi", "ابتدائي متقدم", "t_6", 26),
      ClassEntity("c_7", "فصل رواد سمالوط (خامسة وسادسة)", "samalut", "ابتدائي متقدم", "t_7", 25),
      ClassEntity("c_8", "فصل نجمة الشمال (أولى وتانية)", "beni_mazar", "ابتدائي مبكر", "t_9", 24),
      ClassEntity("c_9", "فصل حماة الإيمان (تالتة ورابعة)", "matay", "ابتدائي متقدم", "t_10", 25),
      ClassEntity("c_10", "فصل شعلة التغيير (إعدادي)", "maghagha", "إعدادي", "t_11", 28),
      ClassEntity("c_11", "فصل فجر دير مواس (خامسة وسادسة)", "dair_mawas", "ابتدائي متقدم", "t_12", 20),
      ClassEntity("c_12", "فصل سفراء الفشن (تالتة ورابعة)", "fashn", "ابتدائي متقدم", "t_13", 26),
      ClassEntity("c_13", "فصل أبطال ببا (أولى وتانية)", "beba", "ابتدائي مبكر", "t_14", 25),
      ClassEntity("c_14", "فصل مشاعل سمسطا (خامسة وسادسة)", "samasta", "ابتدائي متقدم", "t_15", 22)
    )
    dao.insertClasses(classes)

    // 7. Children (diverse demographics: male/female, denominations)
    val children = listOf(
      ChildEntity("ch_1", "ديفيد جون رأفت", "بنين", "أرثوذكس", 2016, "جون رأفت لبيب", "01201112233", "minya_city", "c_1", "شارع عدلي - المنيا", "متميز في الإلقاء"),
      ChildEntity("ch_2", "مارينا عماد زكي", "بنات", "أرثوذكس", 2017, "عماد زكي بطرس", "01099887766", "minya_city", "c_1", "حي شلبي - المنيا", "موهوبة في الرسم"),
      ChildEntity("ch_3", "توماس أيمن عريان", "بنين", "إنجيلي", 2015, "أيمن عريان متى", "01122334455", "minya_city", "c_2", "ميدان بالاس - المنيا", "ملتزم بحضور الورش"),
      ChildEntity("ch_4", "ساندرا نبيل شكري", "بنات", "أرثوذكس", 2014, "نبيل شكري كامل", "01233445566", "minya_city", "c_2", "شارع طه حسين - المنيا", "شخصية قيادية متعاونة"),
      ChildEntity("ch_5", "كيرلس فادي إبراهيم", "بنين", "أرثوذكس", 2014, "فادي إبراهيم حنا", "01103334455", "abu_qurqas", "c_3", "بندر الفكرية - أبوقرقاص", "مهارة استماع ممتازة"),
      ChildEntity("ch_6", "جويرية أشرف وديع", "بنات", "كاثوليك", 2014, "أشرف وديع غطاس", "01044556677", "abu_qurqas", "c_3", "شارع الجمهورية - أبوقرقاص", "مبادرة ونشيطة"),
      ChildEntity("ch_7", "ماركوس تامر فهيم", "بنين", "أرثوذكس", 2017, "تامر فهيم ساويرس", "01255667788", "abu_qurqas", "c_4", "قرية أبيوها - أبوقرقاص", "يحب العمل الجماعي"),
      ChildEntity("ch_8", "كلارا هاني مجدي", "بنات", "إنجيلي", 2017, "هاني مجدي فرج", "01066778899", "abu_qurqas", "c_4", "أبوقرقاص البلد", "حضور منتظم"),
      ChildEntity("ch_9", "بيتر أمير سمير", "بنين", "أرثوذكس", 2012, "أمير سمير فايز", "01005556677", "mallawi", "c_5", "شارع العرفاني - ملوي", "مشارك في مشروع الخدمة"),
      ChildEntity("ch_10", "يوستينا بهجت فوزي", "بنات", "أرثوذكس", 2012, "بهجت فوزي حبيب", "01177889900", "mallawi", "c_5", "حي غرب - ملوي", "متميزة في الحوار"),
      ChildEntity("ch_11", "ستيفن سامي عياد", "بنين", "أرثوذكس", 2015, "سامي عياد توما", "01288990011", "mallawi", "c_6", "شارع تونس - ملوي", "سلوك إيجابي عالي"),
      ChildEntity("ch_12", "ميريت شريف لمعي", "بنات", "كاثوليك", 2015, "شريف لمعي بولس", "01099001122", "mallawi", "c_6", "شارع المجيدي - ملوي", "مبدعة في الفنون"),
      ChildEntity("ch_13", "أبانوب كيرلس نادي", "بنين", "أرثوذكس", 2014, "كيرلس نادي مسعود", "01207778899", "samalut", "c_7", "شارع داوود تكلا - سمالوط", "قائد فريق المسابقات"),
      ChildEntity("ch_14", "مارتينا رامي لمعي", "بنات", "أرثوذكس", 2014, "رامي لمعي حنا", "01100112233", "samalut", "c_7", "قرية دير السيدة العذراء - سمالوط", "ذكاء اجتماعي بارز"),
      ChildEntity("ch_15", "فلوباتير مايكل صفوت", "بنين", "أرثوذكس", 2017, "مايكل صفوت غطاس", "01109990011", "beni_mazar", "c_8", "شارع العاشر - بني مزار", "محب للمشاركة"),
      ChildEntity("ch_16", "جوليانا عصام مكرم", "بنات", "إنجيلي", 2016, "عصام مكرم غالي", "01211223344", "beni_mazar", "c_8", "حي المحطة - بني مزار", "سرعة بديهة"),
      ChildEntity("ch_17", "فيلو رضا شحاتة", "بنين", "أرثوذكس", 2015, "رضا شحاتة شنودة", "01022334455", "matay", "c_9", "شارع الثورة - مطاي", "محب للقراءة"),
      ChildEntity("ch_18", "فيرونيا كمال بطرس", "بنات", "أرثوذكس", 2015, "كمال بطرس موسى", "01211112233", "matay", "c_9", "شارع المدارس - مطاي", "متعاونة ومثابرة"),
      ChildEntity("ch_19", "مينا مارك وجدي", "بنين", "أرثوذكس", 2012, "مارك وجدي فريد", "01012223344", "maghagha", "c_10", "شارع السلام - مغاغة", "مبادر في الأنشطة"),
      ChildEntity("ch_20", "سلفيا جرجس عوض", "بنات", "أرثوذكس", 2013, "جرجس عوض رزق", "01133445566", "maghagha", "c_10", "بندر مغاغة", "حماس قيادي عالي"),
      ChildEntity("ch_21", "كارين صفوت ملاك", "بنات", "أرثوذكس", 2014, "صفوت ملاك أسعد", "01244556677", "dair_mawas", "c_11", "شارع المستشفى - دير مواس", "متميزة أكاديميًا"),
      ChildEntity("ch_22", "جورج إميل جاد", "بنين", "إنجيلي", 2014, "إميل جاد نصيف", "01055667788", "dair_mawas", "c_11", "دير مواس البلد", "مهارات تواصل ممتازة"),
      ChildEntity("ch_23", "أنطون بولا إيليا", "بنين", "أرثوذكس", 2015, "بولا إيليا حكيم", "01214445566", "fashn", "c_12", "شارع البحر - الفشن", "مشارك في المسرح"),
      ChildEntity("ch_24", "ليديا صبحي لمعي", "بنات", "أرثوذكس", 2015, "صبحي لمعي عازر", "01166778899", "fashn", "c_12", "الفشن المحطة", "موهبة إلقاء"),
      ChildEntity("ch_25", "كاراس يوسف جورج", "بنين", "أرثوذكس", 2017, "يوسف جورج سوريال", "01116667788", "samasta", "c_14", "شارع بورسعيد - سمسطا", "شغف بالتعلم"),
      ChildEntity("ch_26", "إيفون فايز حلمي", "بنات", "إنجيلي", 2014, "فايز حلمي ميخائيل", "01277889900", "samasta", "c_14", "سمسطا المركز", "لباقة وروح تعاون"),
      ChildEntity("ch_27", "مهرائيل إبراهيم بشارة", "بنات", "أرثوذكس", 2017, "إبراهيم بشارة تادرس", "01088990011", "beba", "c_13", "شارع السوق - ببا", "مبتسمة وودودة"),
      ChildEntity("ch_28", "يوحنا عماد فخري", "بنين", "كاثوليك", 2016, "عماد فخري مرقس", "01199001122", "beba", "c_13", "حي الفابريقة - ببا", "دقة والتزام عالي")
    )
    dao.insertChildren(children)
  }
}
