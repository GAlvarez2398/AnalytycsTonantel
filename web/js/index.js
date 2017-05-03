/* 
 * Archivo para interactividad del index
 */

//accion para obtener las cookies
function getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for(var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

var app = angular.module('principal', ['ngRoute']);
app.config(function($routeProvider){
    $routeProvider
            .when('/referenciasColaborador', {
                templateUrl : 'vista/referenciasColaborador.html'
            })
            .when('/comisiones', {
                templateUrl : 'vista/comisiones.html'
            })
            .when('/estadisticaCalificacion', {
                templateUrl : 'vista/estadisticaAgencia.html'
            })
            .when('/reporteRally',{
                templateUrl : 'vista/ReporteRally.html'
            })
            .when('/reporteRallyColaborador',{
                templateUrl : 'vista/ReporteRallyColaborador.html'
            });
              
            
});

/*Controlador utilizado para cargar el nombre del usuario
 *Autor Jorge Manuel Alvarez Molina
 *Fecha: 19 de Abril de 2017 
 */
app.controller('nameUserController',function ($scope,$http){
    // Variables de conexion a la base de datos
    $scope.HOST = 'https://module.app-tonantel.com/';
    $scope.Operador = getCookie("Cookie");
    $http({
        url: $scope.HOST+"Analytics/web-service/Data-User/userName?data=" + $scope.Operador,
        method: "GET"
        })
    .then(function(ref){
        $scope.nameUser = ref.data;
    });
});

/*Controlador utilizado para cargar los privilegios
 *Autor Jorge Manuel Alvarez Molina
 *Fecha: 19 de Abril de 2017 
 */
app.controller('privilegiosController',function ($scope,$http){
    $scope.crosselling = false;
    $scope.Rally = false;
    $scope.Service = false;
    
    $scope.Operador = getCookie("Cookie");
   // Variables de conexion a la base de datos
    $scope.HOST = 'https://module.app-tonantel.com/';

    
    $http({
        url: $scope.HOST+"Analytics/web-service/Data-User/Privilegios?data=" + $scope.Operador,
        method: "GET"
        })
    .then(function(ref){
        $scope.privilegios = ref.data;
        $scope.total = $scope.privilegios.length;
        for(var i = 0;i<$scope.total;i++){  
            if($scope.privilegios[i].forma == 'analytics_crosselling'){
                $scope.crosselling =true;    
            }
            else if($scope.privilegios[i].forma == 'analytics_service'){
                $scope.Service = true;
            }
            else if($scope.privilegios[i].forma == 'analytics_Rally'){
                $scope.Rally = true;
            }
        }
    });
});

app.controller('refColController', function($scope, $http){
    // Variables de conexion a la base de datos
    $scope.HOST = 'https://module.app-tonantel.com/';
    
    $http.get($scope.HOST+"Analytics/web-service/lista-agencias")
            .then(function(ag){
                $scope.agencias = ag.data;
            });
    
    $scope.fechaFiltro = new Date();
    $scope.lista = function(){
        $http({
        url: $scope.HOST+"Analytics/web-service/referidos-colaborador?fechaFiltro=" + $scope.fechaFiltro,
        method: "GET"
            })
            .then(function(ref){
                $scope.detalle = ref.data;
            });
    };        
});

// Controlador para cargar la estadistica de Qualification por agencia
app.controller('estAgenciaController', function($scope, $http){
    // Variables de conexion a la base de datos
    $scope.HOST = 'https://module.app-tonantel.com/';
    
    // Accion para cargar las agencias en el select
    $http.get($scope.HOST+"Analytics/web-service/lista-agencias")
            .then(function(ag){
                $scope.agencias = ag.data;
            });
            
    $scope.mostrarResultados = function(){
        // Accion para mostrar la grafica de barras con los datos generales de la agencia
        $http({
            url: $scope.HOST+"Analytics/web-service/estadistica-calificacion/calificacion-total?idAgencia=" + $scope.idSelectAgencia,
            method: "GET"
        })
                .then(function(datos){
                   var misDatos = datos.data;

                    // Codigo para cargar las barras
                    google.charts.load("current", {packages:['corechart']});
                    google.charts.setOnLoadCallback(drawChart);

                    function drawChart() {
                        var data = google.visualization.arrayToDataTable([
                          ["Element", "Density", { role: "style" } ],
                          ["Excelente", misDatos[0].excelente, "blue"],
                          ["Bueno", misDatos[0].bueno, "red"],
                          ["Regular", misDatos[0].regular, "orange"],
                          ["Malo", misDatos[0].malo, "green"]
                        ]);

                        var view = new google.visualization.DataView(data);
                        view.setColumns([0, 1,
                                         { calc: "stringify",
                                           sourceColumn: 1,
                                           type: "string",
                                           role: "annotation" },
                                         2]);

                        var options = {
                          title: "Detalle",
                          width: 600,
                          height: 400,
                          bar: {groupWidth: "95%"},
                          legend: { position: "none" },
                        };
                        var chart = new google.visualization.ColumnChart(document.getElementById("columnchart_values"));
                        chart.draw(view, options);
                    }
                });

        // Accion para mostrar los resultados del departamento de caja por agencia
        $http({
            url: $scope.HOST+"Analytics/web-service/estadistica-calificacion/calificacion-caja?idAgencia=" + $scope.idSelectAgencia,
            method: "GET"
        })
                .then(function(datos){
                    var misDatos = datos.data;

                    google.charts.load('current', {'packages':['corechart']});
                    google.charts.setOnLoadCallback(drawChart);

                    function drawChart() {
                        var data = google.visualization.arrayToDataTable([
                          ['Task', 'Hours per Day'],
                          ['Excelente', misDatos[0].excelente],
                          ['Bueno', misDatos[0].bueno],
                          ['Regular', misDatos[0].regular],
                          ['Malo', misDatos[0].malo]
                        ]);

                        var options = {
                          title: 'Caja'
                        };

                        var chart = new google.visualization.PieChart(document.getElementById('caja'));

                        chart.draw(data, options);
                    }
                });

        // Accion para mostrar los resultados del departamento de secretaria por agencia
        $http({
            url:$scope.HOST+"Analytics/web-service/estadistica-calificacion/calificacion-secretaria?idAgencia=" + $scope.idSelectAgencia,
            method: "GET"
        })
                .then(function(datos){
                    var misDatos = datos.data;

                    google.charts.load('current', {'packages':['corechart']});
                    google.charts.setOnLoadCallback(drawChart);

                    function drawChart() {
                        var data = google.visualization.arrayToDataTable([
                          ['Task', 'Hours per Day'],
                          ['Excelente', misDatos[0].excelente],
                          ['Bueno', misDatos[0].bueno],
                          ['Regular', misDatos[0].regular],
                          ['Malo', misDatos[0].malo]
                        ]);

                        var options = {
                          title: 'Secretaria'
                        };

                        var chart = new google.visualization.PieChart(document.getElementById('secretaria'));

                        chart.draw(data, options);
                    }
                });

        // Metodo para cargar los resultados del departamento de creditos
        $http({
            url:$scope.HOST+"Analytics/web-service/estadistica-calificacion/calificacion-creditos?idAgencia=" + $scope.idSelectAgencia,
            method: "GET"
        })
                .then(function(datos){
                    var misDatos = datos.data;

                    google.charts.load('current', {'packages':['corechart']});
                    google.charts.setOnLoadCallback(drawChart);

                    function drawChart() {
                        var data = google.visualization.arrayToDataTable([
                          ['Task', 'Hours per Day'],
                          ['Excelente', misDatos[0].excelente],
                          ['Bueno', misDatos[0].bueno],
                          ['Regular', misDatos[0].regular],
                          ['Malo', misDatos[0].malo]
                        ]);

                        var options = {
                          title: 'Creditos'
                        };

                        var chart = new google.visualization.PieChart(document.getElementById('creditos'));

                        chart.draw(data, options);
                    }
                });

        // Metodo para mostrar los resultados del departamento de seguros
        $http({
            url: $scope.HOST+"Analytics/web-service/estadistica-calificacion/calificacion-seguros?idAgencia=" + $scope.idSelectAgencia,
            method: "GET"
        })
                .then(function(datos){
                    var misDatos = datos.data;

                    google.charts.load('current', {'packages':['corechart']});
                    google.charts.setOnLoadCallback(drawChart);

                    function drawChart() {
                        var data = google.visualization.arrayToDataTable([
                          ['Task', 'Hours per Day'],
                          ['Excelente', misDatos[0].excelente],
                          ['Bueno', misDatos[0].bueno],
                          ['Regular', misDatos[0].regular],
                          ['Malo', misDatos[0].malo]
                        ]);

                        var options = {
                          title: 'Seguros'
                        };

                        var chart = new google.visualization.PieChart(document.getElementById('seguros'));

                        chart.draw(data, options);
                    }
                });
        }
});

/*Controlador para generar el reporte de comisiones
 * Creado: 22 de febrero del 2017
 * Por: Rigo Galicia
 */

app.controller('comisionesController', function($scope, $http){  
    // Variables de conexion a la base de datos
    $scope.HOST = 'https://module.app-tonantel.com/';
    
    // Accion para consultar el listado de agencias activas y mostrarlas en el select
    $http({
        url: $scope.HOST+"Analytics/web-service/lista-agencias",
        method: "GET" 
    })
    .then(function(datos){
        $scope.agencias = datos.data;
    });
    
    // Accion para consultar las comisiones de los colaboradores
    $scope.generarComision = function(){
        $http({
            url: $scope.HOST+"Analytics/web-service/comisiones/generar-comisiones?fechaInicio=" + $scope.fechaInicio + "&fechaFin=" + $scope.fechaFin,
            method: "GET"
        })
        .then(function(datos){
            $scope.comision = datos.data;
        });
    };
    
});

/*Controlador para las acciones de las metas del rally
 *Creado por: Jorge Manuel Alvarez Molina
 *Fecha: 05 de Abril de 2017
*/
app.controller('RallyController', function($scope, $http){
    // Variables de conexion a la base de datos
    $scope.HOST = 'https://module.app-tonantel.com/';
    // Accion para consultar el listado de agencias activas y mostrarlas en el select
    $http({
        url:$scope.HOST+"Analytics/web-service/lista-agencias",
        method: "GET" 
    })
    .then(function(datos){
        $scope.agencias = datos.data;
    });
    
    // Accion para consultar el listado de metas y mostrarlas en el select     
    $http({
        url: $scope.HOST+"Analytics/web-service/Rally-Report/consultar-metas",
        method: "GET" 
    })
    .then(function(datos){
        $scope.metas = datos.data;
    });
    
    $scope.rallyReport = function (){
        // Accion para consultar el listado de metas por colabrador   
        $http({
            url: $scope.HOST+"Analytics/web-service/Rally-Report/generar-reportRally/?fechaInicio='"+$scope.fechaInicio+"'&fechaFin='"+$scope.fechaFin+"'&meta="+$scope.metaSelect+"&agencia="+$scope.agSelect,
            method: "GET" 
        })
        .then(function(datos){
            $scope.rally = datos.data;
        });
    };
});


/*Controlador para las acciones de las metas del rally por Colaborador
 *Creado por: Jorge Manuel Alvarez Molina
 *Fecha: 11 de Abril de 2017
*/
app.controller('RallyControllerColaborador', function($scope, $http){
    // Variables de conexion a la base de datos
    $scope.HOST = 'https://module.app-tonantel.com/';
    // Accion para consultar el listado de agencias activas y mostrarlas en el select
    $http({
        url: $scope.HOST+"Analytics/web-service/lista-agencias",
        method: "GET" 
    })
    .then(function(datos){
        $scope.agencias = datos.data;
    });
    
    // Accion para consultar el listado de metas y mostrarlas en el select     
    $http({
        url: $scope.HOST+"Analytics/web-service/Rally-Report/consultar-metas",
        method: "GET" 
    })
    .then(function(datos){
        $scope.metas = datos.data;
    });
    
    $scope.rallyReportColaborador = function (){
        // Accion para consultar el listado de metas por colabrador   
        $http({
            url: $scope.HOST+"Analytics/web-service/Rally-Report/consultar-metas-colaborador/?fechaInicio="+$scope.fechaInicio+"&fechaFin="+$scope.fechaFin+"&meta="+$scope.metaSelect+"&agencia="+$scope.agSelect,
            method: "GET" 
        })
        .then(function(datos){
            $scope.rally = datos.data;
        });
    };
});