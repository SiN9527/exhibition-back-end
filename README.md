# Exhibition-Back-End 系統

本專案為學術研討會平台的後端系統，基於 Spring Boot 開發，提供使用者註冊、登入、報名、信件通知等功能，模組化設計支援擴充與微服務整合。

---

## 專案模組架構

| 模組名稱            | 說明                            | 埠號 |
|---------------------|---------------------------------|------|
| `exhibition-user`     | 使用者註冊/登入/JWT 驗證/會員資料維護與查詢   | 8080 |
| `exhibition-signup`   | 會員報名系統                                | 8081 |
| `exhibition-post`   | 會員投稿系統                                  | 8082 |
| `exhibition-review`   | 會員審稿系統                                | 8083 |
| `exhibition-common`   | 共用邏輯（排程、驗證、工具等）               | 8084 |
| `exhibition-notification` | 信件發送、通知管理                      | 80845|

---

## 🚀 專案啟動方式

請先確保安裝好以下工具：

- Java 17+
- Maven 3.8+
- MySQL 8+
- Git

### ✅ 1. Clone 專案

```bash
git clone https://github.com/SiN9527/exhibition-back-end.git
cd exhibition-back-end
