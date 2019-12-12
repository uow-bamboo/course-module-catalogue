import './multiselect-lib';
import $ from 'jquery';

$(() => {
  const onDropdownHide = () => {
    $('form[name=modules]').submit();
  };

  $('.modules-search-form select.multiselect.department')
    .multiselect({
      buttonWidth: '100%', // i really do not like this
      enableClickableOptGroups: true,
      nonSelectedText: 'Any department',
      nSelectedText: 'departments selected',
      allSelectedText: 'All departments',
      onDropdownHide,
    });

  $('.modules-search-form select.multiselect.level')
    .multiselect({
      buttonWidth: '100%',
      nonSelectedText: 'Any level',
      nSelectedText: 'levels selected',
      allSelectedText: 'All levels',
      onDropdownHide,
    });

  $('.modules-search-form select.multiselect.creditValue')
    .multiselect({
      buttonWidth: '100%',
      nonSelectedText: 'Any credit value',
      nSelectedText: 'credit values selected',
      allSelectedText: 'All credit values',
      onDropdownHide,

    });

  $('.modules-search-form select.multiselect.assessmentType')
    .multiselect({
      buttonWidth: '100%',
      dropRight: true,
      nonSelectedText: 'Any assessment type',
      nSelectedText: 'assessment types selected',
      allSelectedText: 'All assessment types',
      onDropdownHide,
    });
});
