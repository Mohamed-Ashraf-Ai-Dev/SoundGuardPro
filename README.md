# SoundGuard Pro

## نظرة عامة على المشروع
تطبيق SoundGuard Pro هو حل تقني متقدم لنظام أندرويد، صُمم خصيصاً لضمان عدم تفويت المكالمات الهاتفية الهامة. يعمل التطبيق من خلال محرك برمجى ذكي يقوم برفع مستوى صوت الرنين إلى الحد الأقصى تلقائياً عند استقبال مكالمات من جهات اتصال محددة، حتى في حال ضبط الهاتف على وضع الصامت أو عدم الإزعاج.

## المميزات التقنية
*   **محرك معالجة أصلي (Native Engine):** يعتمد التطبيق على كود C++ (JNI) لضمان سرعة الاستجابة والتعامل المباشر مع إعدادات الصوت في النظام.
*   **نظام القائمة البيضاء (White-list System):** إمكانية تحديد جهات اتصال معينة لتفعيل ميزة رفع الصوت التلقائي لها دون غيرها.
*   **التوافق مع أندرويد 14:** تم تحديث التطبيق ليدعم أحدث معايير الأمان والخدمات في الخلفية (Foreground Services) لضمان الاستقرار التام.
*   **نظام بناء آلي (CI/CD):** دمج كامل مع GitHub Actions لبناء وتوقيع التطبيق تلقائياً بأعلى معايير الجودة.

## المتطلبات التقنية
*   **نظام التشغيل:** أندرويد 7.0 (API 24) أو أحدث.
*   **لغات البرمجة:** Java 17, C++ (NDK).
*   **أدوات البناء:** Gradle 8.0, Android Gradle Plugin 8.1.0.

## إرشادات البناء والتطوير
المشروع مجهز للعمل مباشرة عبر نظام GitHub Actions. عند إجراء أي تغيير في الكود المصدري، يقوم النظام تلقائياً بالآتي:
1.  التحقق من سلامة الكود وتوافقه.
2.  بناء مكتبات الـ Native باستخدام CMake.
3.  توليد ملفات APK و AAB موقعة رسمياً وجاهزة للنشر.

## الترخيص
جميع الحقوق محفوظة للمطور محمد أشرف.

---

# SoundGuard Pro (English Version)

## Project Overview
SoundGuard Pro is an advanced Android technical solution designed to ensure critical phone calls are never missed. The application utilizes an intelligent software engine that automatically maximizes the ringer volume upon receiving calls from specific contacts, even when the device is set to silent or Do Not Disturb mode.

## Technical Features
*   **Native Processing Engine:** Built with C++ (JNI) to ensure high responsiveness and direct interaction with system audio settings.
*   **White-list Management:** Ability to designate specific contacts for automatic volume maximization.
*   **Android 14 Compatibility:** Updated to support the latest security standards and Foreground Services for maximum stability.
*   **Automated Build System (CI/CD):** Fully integrated with GitHub Actions for automated building and official signing of the application.

## Technical Requirements
*   **Operating System:** Android 7.0 (API 24) or higher.
*   **Programming Languages:** Java 17, C++ (NDK).
*   **Build Tools:** Gradle 8.0, Android Gradle Plugin 8.1.0.

## Build and Development Instructions
The project is configured to work seamlessly with GitHub Actions. Upon any source code changes, the system automatically performs:
1.  Code integrity and compatibility checks.
2.  Native library building using CMake.
3.  Generation of officially signed APK and AAB files ready for deployment.

## License
All rights reserved to the developer, Mohamed Ashraf.
