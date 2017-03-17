var app = angular.module('douglas', ['ngRoute']);

// Init routes
app.config(function ($routeProvider) {
  $routeProvider
    .when('/testresult', {
      templateUrl: 'views/material.html',
      controller: 'MaterialCtrl'
    })
    .when('/products/:productId/test/:testId', {
      templateUrl: 'views/test.html',
      controller: 'TestCtrl'
    })    
    .when('/products/:productId/testresult/:resultId', {
      templateUrl: 'views/result.html',
      controller: 'ResultCtrl'
    })
    .when('/products/:productId', {
      templateUrl: 'views/sections.html',
      controller: 'SectionsCtrl'
    })
    .when('/products', {
      templateUrl: 'views/products.html',
    })
    .otherwise({
      redirectTo: '/products'
    });
});

// REST Service
angular.module('douglas')
.factory('ApiService', ['$http', function ($http) {
  var apiBaseUrl = "rest/v1/";

  return {
    getProducts: function () {
      	return $http.get(apiBaseUrl+"products/");
    },

    getSectionsOfProduct: function (id) {
      	return $http.get(apiBaseUrl+"sections/byProductId/"+id);
    },

    getTest: function (id) {
      	return $http.get(apiBaseUrl+"tests/"+id);
    },

    getResult: function (id) {
      	return $http.get(apiBaseUrl+"testresults/"+id);
    },

    runTest: function (id) {
      	return $http.get(apiBaseUrl+"tests/run/"+id);
    },

    runSection: function (id) {
      	return $http.get(apiBaseUrl+"sections/run/"+id);
    },

    runProduct: function (id) {
      	return $http.get(apiBaseUrl+"products/run/"+id);
    },

    acceptSuggestion: function (id, index) {
      	return $http.get(apiBaseUrl+"suggestions/accept/"+id+"/"+index);
    }
  };
}]);

app.controller('SidebarCtrl', function($scope, ApiService, $routeParams) {
  ApiService.getProducts().then(function(response) {
    $scope.products = response.data;
  });

  // We need to wait until the route is completely changed before assigning stuff
  $scope.$on('$routeChangeSuccess', function(next, current) { 
    $scope.selectedProduct = $routeParams.productId
  });
});

app.controller('SectionsCtrl', function($scope, ApiService, $routeParams) {
  $scope.$on('$routeChangeSuccess', function(next, current) {
    $scope.productId = $routeParams.productId;
    // Request sections via REST
    ApiService.getSectionsOfProduct($scope.productId).then(function(response) {
      $scope.sections = response.data;
    });
  });

  $scope.run = function(id, type, e) {
    // Avoid navigating to an URL but just use the Angular event handlers
    e.preventDefault();
    e.stopPropagation();

    // Depending on what should be runned switch between API endpoints
    if(type == "product") {
      ApiService.runProduct($scope.productId).then(function() {
        alert("Placed all tests in product on run queue");
      });
    } else if (type == "section") {
      ApiService.runSection(id).then(function() {
        alert("Placed all tests in section on run queue");
      });
    } else {
      ApiService.runTest(id).then(function() {
        alert("Placed test on run queue");
      });
    }  
  }
});

app.controller('ResultCtrl', function($scope, ApiService, $routeParams) {
  $scope.$on('$routeChangeSuccess', function(next, current) {
    $scope.productId = $routeParams.productId;
    // Fetch result of test
    ApiService.getResult($routeParams.resultId).then(function(response) {
      $scope.result = response.data;
      $scope.steps = JSON.parse($scope.result.steps);
    });
  });
});

app.controller('TestCtrl', function($scope, ApiService, $routeParams) {
  
  $scope.run = function(id, e) {
    e.preventDefault();
    e.stopPropagation();

    // Run test by ID
    ApiService.runTest(id).then(function() {
      alert("Placed test on run queue");
    });
  }

  // Handle user accepting suggestion
  $scope.acceptSuggestion = function(id, index, e) {
    e.preventDefault();
    e.stopPropagation();

    ApiService.acceptSuggestion(id, index).then(function() {
      window.location.reload();
    });
  }
  
  $scope.$on('$routeChangeSuccess', function(next, current) { 
    $scope.productId = $routeParams.productId;
    $scope.testId = $routeParams.testId;

    // Helper function to reverse order of test results
    $scope.reverse = function(array) {
      var copy = [].concat(array);
      return copy.reverse();
    }

    // Fetch test based on ID in URL
    ApiService.getTest($scope.testId).then(function(response) {
      $scope.test = response.data;
      if($scope.test.testResults.length == 0) {
        $scope.steps = JSON.parse($scope.test.steps);
      } else {
        var lastResult = $scope.test.testResults[$scope.test.testResults.length - 1];
        $scope.steps = JSON.parse(lastResult.steps);
        $scope.currentTestResultId = lastResult.id;
      }
    });
  });
});