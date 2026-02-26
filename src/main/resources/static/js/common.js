document.addEventListener('DOMContentLoaded', function () {
    const detailModal = new bootstrap.Modal(document.getElementById('pinDetailModal'));
    const saveToast = new bootstrap.Toast(document.getElementById('saveToast'));
    const cards = document.querySelectorAll('.btn-open-modal');
    const likeBtn = document.getElementById('likeBtn');
    const likeIcon = document.getElementById('likeIcon');
    const modalImageContainer = document.getElementById('modalImageContainer');
    const giantHeart = document.getElementById('giantHeart');

    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    let currentId = null;
    let lastTap = 0; // Para móvil

    function actualizarEstadoBoton(estaGuardado) {
        if (estaGuardado) {
            likeBtn.className = 'btn btn-danger text-white rounded-circle shadow-sm p-0 pulse-animation';
            likeIcon.className = 'bi bi-heart-fill fs-2';
        } else {
            likeBtn.className = 'btn btn-light text-danger rounded-circle shadow-sm border p-0';
            likeIcon.className = 'bi bi-heart fs-2';
        }
    }

    // FUNCIÓN PARA ENVIAR EL LIKE
    function toggleLike() {
        if (!currentId) return;
        fetch('/like/' + currentId, {
            method: 'POST',
            headers: { [csrfHeader]: csrfToken }
        })
            .then(res => res.json())
            .then(nuevoEstado => {
                actualizarEstadoBoton(nuevoEstado);
                const card = document.querySelector(`.pin-card[data-id="${currentId}"]`);
                if (card) card.setAttribute('data-guardada', nuevoEstado);

                if (nuevoEstado) {
                    saveToast.show();
                    giantHeart.classList.add('animate-heart');
                    setTimeout(() => giantHeart.classList.remove('animate-heart'), 700);
                }
            });
    }

    cards.forEach(card => {
        card.addEventListener('click', function (e) {
            if (e.target.closest('a')) return;

            currentId = this.getAttribute('data-id');
            const nombreAutor = this.getAttribute('data-autor');

            document.getElementById('modalImg').src = this.getAttribute('data-enlace');
            document.getElementById('modalTitulo').innerText = this.getAttribute('data-titulo');
            document.getElementById('modalAutor').innerText = '@' + nombreAutor;
            document.getElementById('modalAutorLink').href = '/usuario/perfil/' + nombreAutor;

            actualizarEstadoBoton(this.getAttribute('data-guardada') === 'true');
            detailModal.show();
        });
    });

    // Evento botón click
    if (likeBtn) likeBtn.addEventListener('click', toggleLike);

    // Evento Doble Click (Escritorio)
    if (modalImageContainer) modalImageContainer.addEventListener('dblclick', toggleLike);

    // Evento Doble Tap (Móvil)
    if (modalImageContainer) modalImageContainer.addEventListener('touchend', function (e) {
        const currentTime = new Date().getTime();
        const tapLength = currentTime - lastTap;
        if (tapLength < 300 && tapLength > 0) {
            toggleLike();
            e.preventDefault();
        }
        lastTap = currentTime;
    });
});
