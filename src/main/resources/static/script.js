function uploadFile(event) {
    const file = event.target.files[0];
    if (!file) return;
    const formData = new FormData();
    formData.append('file', file);

    fetch('/excelStream/upload', {
        method: 'POST',
        body: formData
    }).then(res => location.reload());
}

function editRow(id) {
    alert('수정 기능은 서버 연동 후 구현하세요: ' + id);
}

function deleteRow(id) {
    if (confirm('삭제하시겠습니까?')) {
        fetch(`/delete/${id}`, { method: 'DELETE' })
            .then(res => location.reload());
    }
}

function goToPage(page) {
    window.location.href = `/list?page=${page}`;
}

function uploadToServer() {
    const fileInput = document.getElementById('uploadFile');
    const file = fileInput.files[0];

    if (!file) {
        alert("파일을 선택해주세요.");
        return;
    }

    const formData = new FormData();
    formData.append("file", file);

    fetch("/excelStream/upload", {
        method: "POST",
        body: formData
    })
        .then(res => {
            if (!res.ok) throw new Error("업로드 실패");
            return res.text(); // HTML 렌더링 응답이라면
        })
        .then(html => {
            document.open();
            document.write(html);
            document.close();
        })
        .catch(err => {
            alert("업로드 중 오류가 발생했습니다: " + err.message);
        });
}