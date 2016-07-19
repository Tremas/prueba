'use strict';

describe('Partner Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockPartner, MockTeam;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockPartner = jasmine.createSpy('MockPartner');
        MockTeam = jasmine.createSpy('MockTeam');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Partner': MockPartner,
            'Team': MockTeam
        };
        createController = function() {
            $injector.get('$controller')("PartnerDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'pruebaApp:partnerUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
