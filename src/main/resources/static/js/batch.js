function toggleJob(jobId) {
    fetch(`/schedule/batch/toggle/${jobId}`, { method: "POST" })
        .then(() => location.reload());
}

function openLog(historyId) {
    fetch(`/schedule/batch/log/${historyId}`)
        .then(res => res.text())
        .then(data => {
            const modal = document.getElementById("logModal");
            modal.style.display = "block";
            modal.innerHTML = `
                <div style="
                    background:#fff;
                    margin:10% auto;
                    padding:20px;
                    width:600px;
                    border-radius:12px;">
                    <pre>${data}</pre>
                    <button onclick="closeLog()">닫기</button>
                </div>
            `;
        });
}

function closeLog() {
    document.getElementById("logModal").style.display = "none";
}
function openRegisterModal() {
    document.getElementById('registerModal').style.display = 'flex';
    // 배경 스크롤 방지
    document.body.style.overflow = 'hidden';
}

function closeRegisterModal() {
    document.getElementById('registerModal').style.display = 'none';
    // 배경 스크롤 허용
    document.body.style.overflow = 'auto';
}

// 모달 바깥 영역 클릭 시 닫기
window.onclick = function(event) {
    const modal = document.getElementById('registerModal');
    if (event.target == modal) {
        closeRegisterModal();
    }
}