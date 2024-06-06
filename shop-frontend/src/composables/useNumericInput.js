export default function useNumericInput (){
    const KeysOnlyNumbers = (event)=> {
        if ([46, 8, 9, 27, 13, 110].includes(event.keyCode) ||
        // Allow: Ctrl+A
        (event.keyCode === 65 && event.ctrlKey === true) ||
        // Allow: Ctrl+C
        (event.keyCode === 67 && event.ctrlKey === true) ||
        // Allow: Ctrl+X
        (event.keyCode === 88 && event.ctrlKey === true) ||
        // Allow: Ctrl+V
        (event.keyCode === 86 && event.ctrlKey === true) ||
        // Allow: home, end, left, right
        (event.keyCode >= 35 && event.keyCode <= 39)) {
      // Let it happen, don't do anything
        return;
    }
    if ((event.shiftKey || (event.keyCode < 48 || event.keyCode > 57)) && (event.keyCode < 96 || event.keyCode > 105)) {
        event.preventDefault();
    }}
    return {
        KeysOnlyNumbers
    }
}