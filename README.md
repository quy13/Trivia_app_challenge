# Trivia_app_challenge
là phiên bản cải tiến của Trivia app và thêm những chức năng mới từ challenge như :

-chức năng Hiển Thị Điểm :

mỗi khi sai hay đúng thì sẽ cộng và trừ điểm hiện tại(Current Score) sau đó đi tới câu hỏi tiếp theo với checkAnswer()
và sẽ hiện animation, hiển thị snackBar tùy vào trường hợp đúng hay sai

-chức năng Lưu Điểm Cao : sử dụng SharedPreferences với class Prefs mỗi khi đi ra khỏi app hay onPause 

vd:đang sử dụng app mà có cuộc gọi tới ,nếu đi ra khỏi app thì app sẽ kiểm tra điểm hiện tại so với điểm cao nhất nếu lớn hơn sẽ lưu vào với saveHighestScore()

-chức năng lưu lại/quay lại câu hỏi đang làm : cũng sử dụng SharedPreferences với class Prefs mỗi khi đi ra khỏi app hay onPause 

khi đi ra khỏi app(Hay onPause()) thì sẽ lưu lại câu hỏi đang làm thì sau khi quay lại sẽ trả lại câu hỏi đó và số điểm hiện tại

-1 vài thứ khác :

+có vài nút như Reset thì sẽ làm cho câu hỏi bắt đầu lại (hay quay về 0) ,Fast Foward hay Rewind sẽ đi tới hay quay trở lại 10 câu hỏi
