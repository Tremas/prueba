'use strict';

describe('Player Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockPlayer, MockTeam, MockStadistics;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockPlayer = jasmine.createSpy('MockPlayer');
        MockTeam = jasmine.createSpy('MockTeam');
        MockStadistics = jasmine.createSpy('MockStadistics');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Player': MockPlayer,
            'Team': MockTeam,
            'Stadistics': MockStadistics
        };
        createController = function() {
            $injector.get('$controller')("PlayerDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'pruebaApp:playerUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
