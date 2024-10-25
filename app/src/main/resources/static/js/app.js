$(document).ready(function () {
    // Manejador del evento click para el botón de subir archivo
    $("#uploadButton").click(function () {
        $("#fileInput").click();
    });

    // Cuando el usuario selecciona un archivo, deshabilitar el campo de texto y ajustar el comportamiento
    $("#fileInput").change(function () {
        if (this.files.length > 0) {
            $("#urlInput").prop("disabled", true);
        } else {
            $("#urlInput").prop("disabled", false);
        }
    });

    // Manejador de envío del formulario
    $("#shortener").submit(function (event) {
        event.preventDefault();

        // Si el archivo está seleccionado, procesar el archivo
        if ($("#fileInput").prop("files").length > 0) {
            const file = $('#fileInput')[0].files[0];

            // Verificar si el archivo es un CSV
            if (file.name.endsWith('.csv')) {
                var formData = new FormData();
                formData.append('file', file);

                $.ajax({
                    type: "POST",
                    url: "/api/upload-csv",
                    data: formData,
                    contentType: false,
                    processData: false,
                    success: function (data) {
                        const url = window.URL.createObjectURL(new Blob([data]));
                        const a = document.createElement('a');
                        a.href = url;
                        a.download = 'shortened_urls.csv';
                        document.body.appendChild(a);
                        a.click();
                        document.body.removeChild(a);

                        // Limpiar el campo de entrada de URL
                        $("#urlInput").val('');
                        $("#urlInput").prop("disabled", false); // Habilitar de nuevo el campo
                        $("#fileInput").val(''); // Limpiar el input de archivo
                    },
                    error: function () {
                        $("#result").html("<div class='alert alert-danger lead'>ERROR</div>");
                    }
                });
            } else {
                $("#result").html("<div class='alert alert-danger lead'>Por favor, sube un archivo CSV.</div>");
            }
        } else {
            // Si no hay archivo, procesar como URL
            $.ajax({
                type: "POST",
                url: "/api/link",
                data: $(this).serialize(),
                success: function (msg, status, request) {
                    const imgSrc = `data:image/png;base64,${msg.qrCode}`;
                    $("#result").html(
                        "<div class='alert alert-success lead'><a target='_blank' href='"
                        + request.getResponseHeader('Location')
                        + "'>"
                        + request.getResponseHeader('Location')
                        + `</a></br><img src=${imgSrc} alt='Generated QR Code' /></div>`);

                    // Limpiar el campo de entrada de URL
                    $("#urlInput").val('');
                },
                error: function () {
                    $("#result").html("<div class='alert alert-danger lead'>ERROR</div>");
                }
            });
        }
    });
});
