'use strict';

describe('Coach Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockCoach, MockTeam;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockCoach = jasmine.createSpy('MockCoach');
        MockTeam = jasmine.createSpy('MockTeam');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Coach': MockCoach,
            'Team': MockTeam
        };
        createController = function() {
            $injector.get('$controller')("CoachDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'pruebaApp:coachUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
