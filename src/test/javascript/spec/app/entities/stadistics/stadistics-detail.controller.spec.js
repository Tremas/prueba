'use strict';

describe('Stadistics Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockStadistics, MockGame, MockPlayer;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockStadistics = jasmine.createSpy('MockStadistics');
        MockGame = jasmine.createSpy('MockGame');
        MockPlayer = jasmine.createSpy('MockPlayer');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Stadistics': MockStadistics,
            'Game': MockGame,
            'Player': MockPlayer
        };
        createController = function() {
            $injector.get('$controller')("StadisticsDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'pruebaApp:stadisticsUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
