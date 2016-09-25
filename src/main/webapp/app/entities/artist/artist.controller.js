(function() {
    'use strict';

    angular
        .module('testEsApp')
        .controller('ArtistController', ArtistController);

    ArtistController.$inject = ['$scope', '$state', 'Artist'];

    function ArtistController ($scope, $state, Artist) {
        var vm = this;
        
        vm.artists = [];

        loadAll();

        function loadAll() {
            Artist.query(function(result) {
                vm.artists = result;
            });
        }
    }
})();
